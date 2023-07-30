package org.example.service;

import org.example.Model.Book;
import org.example.Model.BookDto;
import org.example.Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LibraryServiceImplTest {
    private LibraryService libraryService;

    @BeforeEach
    public void setUp() {
        libraryService = new LibraryServiceImpl();
    }

    @Test
    @DisplayName("adding single book")
    public void testAddBook() {
        //given
        BookDto bookDto = new BookDto("Test Book", "Test Author");
        String bookId = libraryService.addBook(bookDto);

        //when
        Book book = libraryService.getBook(bookId);
        Map<Book, Integer> books = libraryService.getBooks();

        //then
        assertNotNull(bookId);
        assertNotNull(book);
        assertEquals("Test Book", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
        assertTrue(books.containsKey(book));
        assertEquals(1, books.get(book));
    }

    @Test
    @DisplayName("adding two the same books to the library -> should increase the number of books by 1")
    public void testAddTwoTheSameBooks() {
        //given
        BookDto bookDto = new BookDto("Test Book", "Test Author");

        //when
        String bookId1 = libraryService.addBook(bookDto);
        String bookId2 = libraryService.addBook(bookDto);
        Book book1 = libraryService.getBook(bookId1);
        Book book2 = libraryService.getBook(bookId2);
        Map<Book, Integer> books = libraryService.getBooks();

        //then
        assertEquals(book1, book2);
        assertTrue(books.containsKey(book1));
        assertEquals(2, books.get(book1));
    }

    @Test
    @DisplayName("adding two different books to the library -> should be two different books in the library")
    public void testAddTwoDifferentBooks() {
        //given
        BookDto bookDto1 = new BookDto("Test Book", "Test Author");
        BookDto bookDto2 = new BookDto("Test Book2", "Test Author2");

        //when
        String bookId1 = libraryService.addBook(bookDto1);
        String bookId2 = libraryService.addBook(bookDto2);
        Book book1 = libraryService.getBook(bookId1);
        Book book2 = libraryService.getBook(bookId2);
        Map<Book, Integer> books = libraryService.getBooks();

        //then
        assertNotEquals(book1, book2);
        assertEquals(1, books.get(book1));
        assertEquals(1, books.get(book2));
    }

    @Test
    public void testAddUser() {
        //given
        String userId = libraryService.addUser("Test User");

        //when
        User user = libraryService.getUser(userId);
        List<User> users = libraryService.getUsers();

        //then
        assertNotNull(userId);
        assertNotNull(user);
        assertEquals("Test User", user.getName());
        assertTrue(users.contains(user));
    }

    @Test
    public void testAddUserFailed() {
        //given
        libraryService.addUser("Test User");

        //then
        assertThrowsExactly(IllegalArgumentException.class, () -> libraryService.addUser("Test User"), "User already exists");
    }

    @Test
    public void testRemoveBook() {
        //given
        BookDto bookDto = new BookDto("Test Book", "Test Author");
        String bookId = libraryService.addBook(bookDto);
        Book book = libraryService.getBook(bookId);
        assertNotNull(book);

        //when
        libraryService.removeBook(bookId);
        book = libraryService.getBook(bookId);
        Map<Book, Integer> books = libraryService.getBooks();

        //then
        assertNull(book);
        assertFalse(books.containsKey(book));
    }

    @Test
    public void testRemoveBookWhenMoreInStock() {
        //given
        BookDto bookDto = new BookDto("Test Book", "Test Author");
        String bookId = libraryService.addBook(bookDto);
        libraryService.addBook(bookDto);

        Book book = libraryService.getBook(bookId);
        assertNotNull(book);

        //when
        libraryService.removeBook(bookId);
        book = libraryService.getBook(bookId);
        Map<Book, Integer> books = libraryService.getBooks();

        //then
        assertNotNull(book);
        assertTrue(books.containsKey(book));
        assertEquals(1, books.get(book));
    }

    @Test
    public void testRemoveBookFailed() {
        assertThrowsExactly(IllegalArgumentException.class, () -> libraryService.removeBook("123"), "Book not found");
    }

    @Test
    public void testRemoveUser() {
        //given
        String userId = libraryService.addUser("Test User");

        User user = libraryService.getUser(userId);
        assertNotNull(user);

        //when
        libraryService.removeUser(userId);
        user = libraryService.getUser(userId);
        List<User> users = libraryService.getUsers();

        //then
        assertNull(user);
        assertFalse(users.contains(user));
    }

    @Test
    public void testRemoveUserFailed() {
        assertThrowsExactly(IllegalArgumentException.class, () -> libraryService.removeUser("123"), "User not found");
    }

    @Test
    public void testRentBook() {
        //given
        BookDto bookDto = new BookDto("Test Book", "Test Author");
        String bookId = libraryService.addBook(bookDto);
        String userId = libraryService.addUser("Test User");

        //when
        String rentedBookId = libraryService.rentBook(bookId, userId);
        Map<Book, LocalDate> rentedBooks = libraryService.getRentedBooks();
        Map<Book, Integer> books = libraryService.getBooks();
        User user = libraryService.getUser(userId);
        String userRentedBookId = user.getSingleRentedBook(bookId);

        //then
        assertNotNull(rentedBookId);
        assertEquals(bookId, rentedBookId);
        assertEquals(1, rentedBooks.size());
        assertEquals(userRentedBookId, rentedBookId);
    }

    @Test
    @DisplayName("user not found when renting a book -> should throw exception")
    public void testRentBookFailedUserNotFound() {
        //given
        BookDto bookDto = new BookDto("Test Book", "Test Author");
        String bookId = libraryService.addBook(bookDto);

        //then
        assertThrowsExactly(IllegalArgumentException.class, () -> libraryService.rentBook(bookId, "123"), "User not found");
    }

    @Test
    @DisplayName("book not found when renting a book -> should throw exception")
    public void testRentBookFailedBookNotFound() {
        //given
        String userId = libraryService.addUser("Test User");

        //then
        assertThrowsExactly(IllegalArgumentException.class, () -> libraryService.rentBook("123", userId), "Book not found");
    }

    @Test
    @DisplayName("user is already renting a book -> should throw exception")
    public void testRentBookFailedUserAlreadyRenting() {
        //given
        BookDto bookDto = new BookDto("Test Book", "Test Author");
        String bookId = libraryService.addBook(bookDto);
        String userId = libraryService.addUser("Test User");

        //when
        libraryService.rentBook(bookId, userId);
        IllegalArgumentException exception = assertThrowsExactly(IllegalArgumentException.class, () -> libraryService.rentBook(bookId, userId));

        //then
        assertEquals(IllegalArgumentException.class, exception.getClass());
        assertEquals("User is already renting a book", exception.getMessage());
    }

    @Test
    @DisplayName("book is not in stock -> should throw exception")
    public void testRentBookFailedBookNotInStock() {
        //given
        BookDto bookDto = new BookDto("Test Book", "Test Author");
        String bookId = libraryService.addBook(bookDto);
        String userId = libraryService.addUser("Test User");
        String userId2 = libraryService.addUser("Test User2");

        //when
        libraryService.rentBook(bookId, userId);
        IllegalArgumentException exception = assertThrowsExactly(IllegalArgumentException.class, () -> libraryService.rentBook(bookId, userId2));

        //then
        assertEquals(IllegalArgumentException.class, exception.getClass());
        assertEquals("Book is not available", exception.getMessage());
    }

    @Test
    @DisplayName("user return book -> should return book to stock")
    public void testReturnBook() {
        //given
        BookDto bookDto = new BookDto("Test Book", "Test Author");
        String bookId = libraryService.addBook(bookDto);
        String userId = libraryService.addUser("Test User");
        libraryService.rentBook(bookId, userId);

        //when
        libraryService.returnBook(bookId, userId);
        Map<Book, Integer> books = libraryService.getBooks();
        Map<Book, LocalDate> rentedBooks = libraryService.getRentedBooks();
        User user = libraryService.getUser(userId);
        String userRentedBookId = user.getSingleRentedBook(bookId);

        //then
        assertEquals(1, books.size());
        assertEquals(1, books.get(libraryService.getBook(bookId)));
        assertEquals(0, rentedBooks.size());
        assertNull(userRentedBookId);
    }

    @Test
    @DisplayName("user not found when returning a book -> should throw exception")
    public void testReturnBookFailedUserNotFound() {
        //given
        BookDto bookDto = new BookDto("Test Book", "Test Author");
        String bookId = libraryService.addBook(bookDto);

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> libraryService.returnBook(bookId, "123"));

        //then
        assertEquals(IllegalArgumentException.class, exception.getClass());
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    @DisplayName("book not found when returning a book -> should throw exception")
    public void testReturnBookFailedBookNotFound() {
        //given
        String userId = libraryService.addUser("Test User");

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> libraryService.returnBook("123", userId));

        //then
        assertEquals(IllegalArgumentException.class, exception.getClass());
        assertEquals("Book not found", exception.getMessage());
    }

    @Test
    @DisplayName("user is not renting a book -> should throw exception")
    public void testReturnBookFailedUserNotRenting() {
        //given
        BookDto bookDto = new BookDto("Test Book", "Test Author");
        String bookId = libraryService.addBook(bookDto);
        String userId = libraryService.addUser("Test User");

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> libraryService.returnBook(bookId, userId));

        //then
        assertEquals(IllegalArgumentException.class, exception.getClass());
        assertEquals("Book not found", exception.getMessage());
    }

    @Test
    @DisplayName("user got penalty after returning a book")
    public void testReturnBookFailedUserGotPenalty() {
        //given
        BookDto bookDto = new BookDto("Test Book", "Test Author");
        String bookId = libraryService.addBook(bookDto);
        String userId = libraryService.addUser("Test User");
        libraryService.rentBook(bookId, userId);

        //when
        libraryService.setBookDueDate(bookId,LocalDate.now().minusDays(20));
        libraryService.returnBook(bookId, userId);
        User user = libraryService.getUser(userId);

        //then
        assertEquals(20, user.getPenaltyPoints());
        assertTrue(user.hasBeenBlocked());
    }
}