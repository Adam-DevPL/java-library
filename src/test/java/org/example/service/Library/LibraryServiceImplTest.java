package org.example.service.Library;

import org.example.Model.Book.BookDto;
import org.example.Model.User.UserDto;
import org.example.service.Book.BookStockService;
import org.example.service.Book.BookStockServiceImpl;
import org.example.service.Booking.BookingService;
import org.example.service.Booking.BookingServiceImpl;
import org.example.service.User.UserService;
import org.example.service.User.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LibraryServiceImplTest {

    private LibraryService libraryService;

    @BeforeEach
    void setUp() {
        BookingService bookingService = new BookingServiceImpl();
        UserService userService = new UserServiceImpl();
        BookStockService bookStockService = new BookStockServiceImpl();
        libraryService = new LibraryServiceImpl(bookStockService, userService, bookingService);
    }

    @Test
    @DisplayName("Test for addBook")
    void addBook() {
        // given
        BookDto bookDto = new BookDto("title", "author");

        // when
        String newBookId = libraryService.addBook(bookDto);

        // then
        assertEquals(1, libraryService.getBooksStock().size());
    }

    @Test
    @DisplayName("Test for removeBook")
    void removeBook() {
        // given
        BookDto bookDto = new BookDto("title", "author");
        String newBookId = libraryService.addBook(bookDto);

        // when
        boolean isRemoved = libraryService.removeBook(newBookId);

        // then
        assertTrue(isRemoved);
        assertEquals(0, libraryService.getBooksStock().size());
    }

    @Test
    @DisplayName("Test for addUser")
    void addUser() {
        // given
        UserDto name = new UserDto("name");

        // when
        String newUserId = libraryService.addUser(name);

        // then
           assertEquals(1, libraryService.getUsers().size());

    }

    @Test
    @DisplayName("Test for removeUser")
    void removeUser() {
        // given
        UserDto name = new UserDto("name");
        String newUserId = libraryService.addUser(name);

        // when
        boolean isRemoved = libraryService.removeUser(newUserId);

        // then
        assertTrue(isRemoved);
        assertEquals(0, libraryService.getUsers().size());
    }
}