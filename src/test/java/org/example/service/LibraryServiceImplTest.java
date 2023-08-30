package org.example.service;

import org.example.Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LibraryServiceImplTest {
    private LibraryService libraryService;
    private BookService bookService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl();
        userService = new UserServiceImpl();
        libraryService = new LibraryServiceImpl(bookService, userService);
    }

    @Test
    @DisplayName("Add book")
    void addBook() {
        //given
        BookDto bookDto = new BookDto("title", "author");

        //when
        String bookId = libraryService.addBook(bookDto);
        List<BookStock> booksStock = libraryService.getBooksStock();

        //then
        assertNotNull(bookId);
        assertEquals(1, booksStock.size());
        assertEquals(1, booksStock.get(0).getQuantity());
        assertEquals(bookId, booksStock.get(0).getBook().getId());
    }

    @Test
    @DisplayName("Add book - multiple books")
    void addBook_multipleBooks() {
        //given
        BookDto bookDto = new BookDto("title", "author");
        BookDto bookDto2 = new BookDto("title", "author");

        //when
        String bookId = libraryService.addBook(bookDto);
        String bookId2 = libraryService.addBook(bookDto2);
        List<BookStock> booksStock = libraryService.getBooksStock();

        //then
        assertNotNull(bookId);
        assertNotNull(bookId2);
        assertEquals(1, booksStock.size());
        assertEquals(2, booksStock.get(0).getQuantity());
        assertEquals(bookId, booksStock.get(0).getBook().getId());
        assertEquals(bookId2, booksStock.get(0).getBook().getId());
    }

    @Test
    @DisplayName("Add book - multiple books - remove one")
    void addBook_multipleBooks_removeOne() {
        //given
        BookDto bookDto = new BookDto("title", "author");
        BookDto bookDto2 = new BookDto("title", "author");

        //when
        String bookId = libraryService.addBook(bookDto);
        String bookId2 = libraryService.addBook(bookDto2);
        libraryService.removeBook(bookId);
        List<BookStock> booksStock = libraryService.getBooksStock();

        //then
        assertNotNull(bookId);
        assertNotNull(bookId2);
        assertEquals(1, booksStock.size());
        assertEquals(1, booksStock.get(0).getQuantity());
        assertEquals(bookId2, booksStock.get(0).getBook().getId());
    }

    @Test
    @DisplayName("Add book - multiple books - remove all")
    void addBook_multipleBooks_removeAll() {
        //given
        BookDto bookDto = new BookDto("title", "author");
        BookDto bookDto2 = new BookDto("title", "author");

        //when
        String bookId = libraryService.addBook(bookDto);
        String bookId2 = libraryService.addBook(bookDto2);
        libraryService.removeBook(bookId);
        libraryService.removeBook(bookId2);
        List<BookStock> booksStock = libraryService.getBooksStock();

        //then
        assertNotNull(bookId);
        assertNotNull(bookId2);
        assertEquals(0, booksStock.size());
    }

    @Test
    @DisplayName("remove book when not in stock - throw exception")
    void removeBook_notInStock() {
        //given
        String bookId = "bookId";
        //then
        assertThrows(IllegalArgumentException.class, () -> libraryService.removeBook(bookId));
    }

    @Test
    @DisplayName("add user")
    void addUser() {
        //given
        String userName = "userName";
        UserDto userDto = new UserDto(userName);

        //when
        String userId = libraryService.addUser(userDto);
        List<User> users = libraryService.getUsers();

        //then
        assertNotNull(userId);
        assertEquals(1, users.size());
        assertEquals(userId, users.get(0).getId());
        assertEquals(userName, users.get(0).getName());
    }

    @Test
    @DisplayName("add the same user twice - throw exception")
    void addUser_theSameUserTwice() {
        //given
        String userName = "userName";
        UserDto userDto = new UserDto(userName);

        //when
        String userId = libraryService.addUser(userDto);
        assertThrows(IllegalArgumentException.class, () -> libraryService.addUser(userDto));
    }

    @Test
    @DisplayName("remove user")
    void removeUser() {
        //given
        String userName = "userName";
        UserDto userDto = new UserDto(userName);

        //when
        String userId = libraryService.addUser(userDto);
        boolean isSuccess = libraryService.removeUser(userId);
        List<User> users = libraryService.getUsers();

        //then
        assertNotNull(userId);
        assertTrue(isSuccess);
        assertEquals(0, users.size());
    }

    @Test
    @DisplayName("remove not existing user")
    void removeUser_notExistingUser() {
        //then
        assertThrows(IllegalArgumentException.class, () -> libraryService.removeUser("userId"));
    }

    @Test
    @DisplayName("borrow book")
    void borrowBook() {
        //given
        String userName = "userName";
        UserDto userDto = new UserDto(userName);
        String userId = libraryService.addUser(userDto);
        BookDto bookDto = new BookDto("title", "author");
        String bookId = libraryService.addBook(bookDto);

        //when
        libraryService.borrowBook(bookId, userId);
        List<BookStock> booksStock = libraryService.getBooksStock();
        Map<String, LocalDate> borrowedBooks = libraryService.getBorrowedBooks(userId);

        //then
        assertEquals(0, booksStock.get(0).getQuantity());
        assertEquals(1, borrowedBooks.size());
        assertEquals(bookId, borrowedBooks.keySet().iterator().next());
    }

    @Test
    @DisplayName("borrow book - multiple books")
    void borrowBook_multipleBooks() {
        //given
        String userName = "userName";
        UserDto userDto = new UserDto(userName);
        String userId = libraryService.addUser(userDto);
        BookDto bookDto = new BookDto("title", "author");
        String bookId = libraryService.addBook(bookDto);
        BookDto bookDto2 = new BookDto("title2", "author");
        String bookId2 = libraryService.addBook(bookDto2);

        //when
        libraryService.borrowBook(bookId, userId);
        libraryService.borrowBook(bookId2, userId);
        List<BookStock> booksStock = libraryService.getBooksStock();
        Map<String, LocalDate> borrowedBooks = libraryService.getBorrowedBooks(userId);

        //then
        assertEquals(0, booksStock.get(0).getQuantity());
        assertEquals(0, booksStock.get(1).getQuantity());
        assertEquals(2, borrowedBooks.size());
        assertTrue(borrowedBooks.containsKey(bookId));
        assertTrue(borrowedBooks.containsKey(bookId2));
    }

    @Test
    @DisplayName("borrow book - multiple books - remove one")
    void borrowBook_multipleBooks_removeOne() {
        //given
        String userName = "userName";
        UserDto userDto = new UserDto(userName);
        String userId = libraryService.addUser(userDto);
        BookDto bookDto = new BookDto("title", "author");
        String bookId = libraryService.addBook(bookDto);
        BookDto bookDto2 = new BookDto("title2", "author");
        String bookId2 = libraryService.addBook(bookDto2);

        //when
        libraryService.borrowBook(bookId, userId);
        libraryService.borrowBook(bookId2, userId);
        libraryService.returnBook(bookId, userId);
        List<BookStock> booksStock = libraryService.getBooksStock();
        Map<String, LocalDate> borrowedBooks = libraryService.getBorrowedBooks(userId);

        //then
        assertEquals(2, booksStock.size());
        assertEquals(1, booksStock.get(0).getQuantity());
        assertEquals(1, borrowedBooks.size());
        assertTrue(borrowedBooks.containsKey(bookId2));
    }
}