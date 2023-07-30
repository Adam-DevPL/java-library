package org.example.Model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class User {
    private final String id;
    private final String name;
    private int penaltyPoints;
    private LocalDate blockedUntil;

    private final List<String> rentedBooks = new ArrayList<>();

    public User(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.penaltyPoints = 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPenaltyPoints() {
        return penaltyPoints;
    }

    public boolean hasBeenBlocked() {
        if (blockedUntil == null) {
            return false;
        }

        int diff = (int) ChronoUnit.DAYS.between(LocalDate.now(), blockedUntil);

        return diff <= 0;
    }

    public void addPenaltyPoints(int penaltyPoints) {
        this.penaltyPoints += penaltyPoints;

        if (this.penaltyPoints >= 10) {
            this.blockedUntil = LocalDate.now().plusDays(30);
        }
    }

    public void addBook(String bookId) {
        rentedBooks.add(bookId);
    }

    public void removeBook(String bookId) {
        rentedBooks.remove(bookId);
    }

    public List<String> getRentedBooks() {
        return rentedBooks;
    }

    public String getSingleRentedBook(String bookId) {
        return rentedBooks.stream()
                .filter(b -> b.equals(bookId))
                .findFirst()
                .orElse(null);
    }
}
