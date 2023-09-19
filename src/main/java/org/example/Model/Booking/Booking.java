package org.example.Model.Booking;

import java.time.LocalDate;
import java.util.*;

public class Booking {
    private final Map<String, LocalDate> books = new HashMap<>();
    private final String userId;
    private int penaltyPoints;
    private LocalDate blockedUntil;

    public Booking(String bookId, String userId) {
        this.books.put(bookId, LocalDate.now().plusDays(7));
        this.userId = userId;
        this.penaltyPoints = 0;
        this.blockedUntil = null;
    }

    public Map<String, LocalDate> getBooks() {
        return books;
    }

    public String getUserId() {
        return userId;
    }

    public void setPenaltyPoints(int penaltyPoints) {
        this.penaltyPoints = penaltyPoints;
    }
    public int getPenaltyPoints() {
        return penaltyPoints;
    }

    public void setBlockedUntil(LocalDate blockedUntil) {
        this.blockedUntil = blockedUntil;
    }

    public LocalDate getDate() {
        return blockedUntil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(books, booking.books) && Objects.equals(userId, booking.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(books, userId);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "books=" + books +
                ", userId='" + userId + '\'' +
                ", penaltyPoints=" + penaltyPoints +
                ", blockedUntil=" + blockedUntil +
                '}';
    }
}
