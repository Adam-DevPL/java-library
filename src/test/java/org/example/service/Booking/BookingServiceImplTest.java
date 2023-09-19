package org.example.service.Booking;

import org.example.Model.Booking.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingServiceImplTest {

    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImpl();
    }

    @Test
    @DisplayName("Test for borrowBook")
    void borrowBook() {
        // given
        String bookId = "bookId";
        String userId = "userId";

        // when
        bookingService.borrowBook(bookId, userId);

        // then
        assertEquals(1, bookingService.getBookings().size());
        assertEquals(userId, bookingService.getBookings().get(0).getUserId());
    }

    @Test
    @DisplayName("Test for returnBook")
    void returnBook() {
        // given
        String bookId = "bookId";
        String userId = "userId";
        bookingService.borrowBook(bookId, userId);

        // when
        boolean isReturned = bookingService.returnBook(bookId, userId);

        // then
        assertTrue(isReturned);
        assertEquals(0, bookingService.getBookings().get(0).getBooks().size());
    }

    @Test
    @DisplayName("Test for returnBook when user does not have any books rented")
    void returnBookWhenUserDoesNotHaveAnyBooksRented() {
        // given
        String bookId = "bookId";
        String userId = "userId";

        // when
        Exception exception = assertThrows(Exception.class, () -> bookingService.returnBook(bookId, userId));

        // then
        assertEquals("User does not have any books rented", exception.getMessage());
    }

    @Test
    @DisplayName("Test for returnBook when book does not exist")
    void returnBookWhenBookDoesNotExist() {
        // given
        String bookId = "bookId";
        String userId = "userId";
        bookingService.borrowBook(bookId, userId);

        // when
        Exception exception = assertThrows(Exception.class, () -> bookingService.returnBook("wrongBookId", userId));

        // then
        assertEquals("User does not have this book rented", exception.getMessage());
    }


    @Test
    @DisplayName("Test for borrow when user is blocked")
    void returnBookWhenUserIsBlocked() {
        // given
        String bookId = "bookId";
        String userId = "userId";
        bookingService.borrowBook(bookId, userId);

        bookingService.getBookings().get(0).setBlockedUntil(LocalDate.now().plusDays(30));

        // when
        Exception exception = assertThrows(Exception.class, () -> bookingService.borrowBook("differentBook", userId));

        // then
        assertEquals("User is blocked", exception.getMessage());
    }

    @Test
    @DisplayName("Test for getBookings")
    void getBookings() {
        // given
        String bookId = "bookId";
        String userId = "userId";
        bookingService.borrowBook(bookId, userId);

        // when
        var bookings = bookingService.getBookings();

        // then
        assertEquals(1, bookings.size());
    }

}