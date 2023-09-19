package org.example.service.Booking;

import org.example.Model.Booking.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface BookingService {
    Booking borrowBook(String bookId, String userId);
    boolean returnBook(String bookId, String userId);
    List<Booking> getBookings();
}
