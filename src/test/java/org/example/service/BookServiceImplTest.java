package org.example.service;

import org.example.Model.Book;
import org.example.Model.BookDto;
import org.example.Model.BookStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceImplTest {

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl();
    }

    @Test
    @DisplayName("Add book")
    void addBook() {
        //given
        BookDto bookDto = new BookDto("title", "author");

        //when
        String bookId = bookService.addBook(bookDto);
        Optional<List<BookStock>> booksStock = bookService.getBooksStock();

        //then
        assertNotNull(bookId);
        assertTrue(booksStock.isPresent());
        assertEquals(1, booksStock.get().size());
        assertEquals(1, booksStock.get().get(0).getQuantity());
        assertEquals(bookId, booksStock.get().get(0).getBook().getId());
    }

    @Test
    @DisplayName("Add book - multiple books")
    void addBook_multipleBooks() {
        //given
        BookDto bookDto = new BookDto("title", "author");
        BookDto bookDto2 = new BookDto("title", "author");

        //when
        String bookId = bookService.addBook(bookDto);
        String bookId2 = bookService.addBook(bookDto2);
        Optional<List<BookStock>> booksStock = bookService.getBooksStock();

        //then
        assertNotNull(bookId);
        assertNotNull(bookId2);
        assertTrue(booksStock.isPresent());
        assertEquals(1, booksStock.get().size());
        assertEquals(2, booksStock.get().get(0).getQuantity());
        assertEquals(bookId, booksStock.get().get(0).getBook().getId());
    }

    @Test
    @DisplayName("Remove book")
    void removeBook() {
        //given
        BookDto bookDto = new BookDto("title", "author");
        String bookId = bookService.addBook(bookDto);

        //when
        boolean result = bookService.removeBook(bookId);
        Optional<List<BookStock>> booksStock = bookService.getBooksStock();

        //then
        assertTrue(result);
        assertTrue(booksStock.isPresent());
        assertEquals(0, booksStock.get().size());
    }

    @Test
    @DisplayName("Remove book mot in BookStock")
    void removeBook_notInBookStock() {

        //when
        boolean result = bookService.removeBook("notInBookStock");

        //then
        assertFalse(result);
    }

    @Test
    @DisplayName("get one book from BookStock")
    void getBook() {
        //given
        BookDto bookDto = new BookDto("title", "author");
        String bookId = bookService.addBook(bookDto);

        //when
        Optional<Book> bookStock = bookService.getBook(bookId);

        //then
        assertTrue(bookStock.isPresent());
        assertEquals(bookId, bookStock.get().getId());
    }

    @Test
    @DisplayName("get one book from BookStock - not found")
    void getBook_notFound() {
        //when
        Optional<Book> bookStock = bookService.getBook("notFound");

        //then
        assertTrue(bookStock.isEmpty());
    }

    @Test
    @DisplayName("get all books from BookStock")
    void getBooksStock() {
        //given
        BookDto bookDto = new BookDto("title", "author");
        BookDto bookDto2 = new BookDto("title", "author");
        bookService.addBook(bookDto);
        bookService.addBook(bookDto2);

        //when
        Optional<List<BookStock>> booksStock = bookService.getBooksStock();

        //then
        assertTrue(booksStock.isPresent());
        assertEquals(1, booksStock.get().size());
        assertEquals(2, booksStock.get().get(0).getQuantity());
    }

    @Test
    @DisplayName("get all books from BookStock - empty")
    void getBooksStock_empty() {
        //when
        Optional<List<BookStock>> booksStock = bookService.getBooksStock();

        //then
        assertTrue(booksStock.isPresent());
        assertEquals(0, booksStock.get().size());
    }
}