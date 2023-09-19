package org.example.service.Book;

import org.example.Model.Book.Book;
import org.example.Model.Book.BookDto;
import org.example.service.Utils.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookStockServiceImplTest {

    private BookStockServiceImpl bookStockService;

    @BeforeEach
    void setUp() {
        bookStockService = new BookStockServiceImpl();
    }

    @Test
    @DisplayName("Test for addBook")
    void addBook() {
        // given
        BookDto bookDto = new BookDto("title", "author");

        // when
        String newBookId = bookStockService.addBook(bookDto);

        // then
        assertEquals(1, bookStockService.getBooksStock().size());
        assertEquals(newBookId, bookStockService.getBooksStock().get(0).getBook().getId());
        assertEquals("title", bookStockService.getBooksStock().get(0).getBook().getTitle());
        assertEquals("author", bookStockService.getBooksStock().get(0).getBook().getAuthor());
    }

    @Test
    @DisplayName("Test for removeBook")
    void removeBook() {
        // given
        BookDto bookDto = new BookDto("title", "author");
        String newBookId = bookStockService.addBook(bookDto);

        // when
        boolean isRemoved = bookStockService.removeBook(newBookId);

        // then
        assertTrue(isRemoved);
        assertEquals(0, bookStockService.getBooksStock().size());
    }

    @Test
    @DisplayName("Test for getBook")
    void getBook() {
        // given
        BookDto bookDto = new BookDto("title", "author");
        String newBookId = bookStockService.addBook(bookDto);

        // when
        Optional<Book> book = bookStockService.getBook(newBookId);

        // then
        assertTrue(book.isPresent());
        assertEquals(newBookId, book.get().getId());
    }

    @Test
    @DisplayName("Test for getBook when book does not exist")
    void getBookWhenBookDoesNotExist() {
        // then
        assertThrowsExactly(CustomException.class, () -> bookStockService.getBook("wrongId"), "Book does not exist");
    }

    @Test
    @DisplayName("Test for getBook when book is out of stock")
    void getBookWhenBookIsOutOfStock() {
        // then
        assertThrowsExactly(CustomException.class, () -> bookStockService.getBook("wrongId"), "Book is out of stock");
    }

    @Test
    @DisplayName("Test for getBooksStock")
    void getBooksStock() {
        // given
        BookDto bookDto = new BookDto("title", "author");
        bookStockService.addBook(bookDto);

        // when
        var booksStock = bookStockService.getBooksStock();

        // then
        assertEquals(1, booksStock.size());
    }

    @Test
    @DisplayName("Test for increaseQuantity")
    void increaseQuantity() {
        // given
        BookDto bookDto = new BookDto("title", "author");
        String newBookId = bookStockService.addBook(bookDto);

        // when
        bookStockService.increaseQuantity(newBookId);

        // then
        assertEquals(2, bookStockService.getBooksStock().get(0).getQuantity());
    }

    @Test
    @DisplayName("Test for decreaseQuantity")
    void decreaseQuantity() {
        // given
        BookDto bookDto = new BookDto("title", "author");
        String newBookId = bookStockService.addBook(bookDto);

        // when
        bookStockService.decreaseQuantity(newBookId);

        // then
        assertEquals(0, bookStockService.getBooksStock().get(0).getQuantity());
    }

    @Test
    @DisplayName("Test for decreaseQuantity when book does not exist")
    void decreaseQuantityWhenBookDoesNotExist() {
        // given
        BookDto bookDto = new BookDto("title", "author");
        bookStockService.addBook(bookDto);

        // when
        assertThrows(Exception.class, () -> bookStockService.decreaseQuantity("wrongId"));
    }

    @Test
    @DisplayName("Test for decreaseQuantity when book is out of stock")
    void decreaseQuantityWhenBookIsOutOfStock() {
        // given
        BookDto bookDto = new BookDto("title", "author");
        String newBookId = bookStockService.addBook(bookDto);
        bookStockService.removeBook(newBookId);

        // when
        assertThrows(Exception.class, () -> bookStockService.decreaseQuantity(newBookId));
    }

}