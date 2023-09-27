package org.example.service.Booking;

import org.example.Model.Booking.Booking;
import org.example.service.Utils.exceptions.BookingBookRentedException;
import org.example.service.Utils.exceptions.BookingUserNotAnyBookRentedException;
import org.example.service.Utils.exceptions.BookingUserNotBookRentedException;
import org.example.service.Utils.exceptions.BookingUserIsBlockedException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BookingServiceImpl implements BookingService {

    private final List<Booking> rentedBooks = new ArrayList<>();


    @Override
    public Booking borrowBook(String bookId, String userId) {
        Optional<Booking> booking = rentedBooks.stream()
                .filter(b -> b.getUserId().equals(userId))
                .findFirst();

        if (booking.isEmpty()) {
            Booking newBooking = new Booking(bookId, userId);
            rentedBooks.add(newBooking);
            return newBooking;
        }

        long totalDaysDifference = ChronoUnit.DAYS.between(booking.get().getDate(), LocalDate.now());

        if (totalDaysDifference > 0) {
            throw new BookingUserIsBlockedException(new IllegalArgumentException("User is blocked"));
        }

        Optional<Map.Entry<String, LocalDate>> foundBook = booking.get().getBooks().entrySet().stream()
                .filter(b -> b.getKey().equals(bookId))
                .findFirst();

        if (foundBook.isPresent()) {
            throw new BookingBookRentedException(new IllegalArgumentException("Book is already rented"));
        }

        booking.get().getBooks().put(bookId, LocalDate.now().plusDays(7));

        return booking.get();
    }

    @Override
    public boolean returnBook(String bookId, String userId) {
        Optional<Booking> booking = rentedBooks.stream()
                .filter(b -> b.getUserId().equals(userId))
                .findFirst();

        if (booking.isEmpty()) {
            throw new BookingUserNotAnyBookRentedException(new IllegalArgumentException("User does not have any book rented"));
        }

        Optional<Map.Entry<String, LocalDate>> foundBook = booking.get().getBooks().entrySet().stream()
                .filter(b -> b.getKey().equals(bookId))
                .findFirst();

        if (foundBook.isEmpty()) {
            throw new BookingUserNotBookRentedException(new IllegalArgumentException("User does not have this book rented"));
        }

        long totalDaysDifference = ChronoUnit.DAYS.between(foundBook.get().getValue(), LocalDate.now());

        if (totalDaysDifference > 0) {
            booking.get().setPenaltyPoints(booking.get().getPenaltyPoints() + (int) totalDaysDifference);
        }

        int penaltyPoints = booking.get().getPenaltyPoints();

        if (penaltyPoints > 10) {
            booking.get().setBlockedUntil(LocalDate.now().plusDays(30));
        }

        booking.get().getBooks().remove(bookId);

        return true;
    }

    @Override
    public List<Booking> getBookings() {
        return rentedBooks;
    }
}
