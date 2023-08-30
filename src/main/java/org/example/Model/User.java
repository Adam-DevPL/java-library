package org.example.Model;

import java.time.LocalDate;
import java.util.*;

public class User {
    private final String id;
    private final String name;
    private final Map<String, LocalDate> rentedBooks = new HashMap<>();
    private int penaltyPoints;
    private LocalDate blockedUntil;

    public User(UserDto userDto) {
        this.id = UUID.randomUUID().toString();
        this.name = userDto.name();
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

    public void setBlockedUntil(LocalDate blockedUntil) {
        this.blockedUntil = blockedUntil;
    }

    public LocalDate getBlockedUntil() {
        return blockedUntil;
    }

    public void setPenaltyPoints(int penaltyPoints) {
        this.penaltyPoints = penaltyPoints;
    }

    public void addRentedBook(String bookId) {
        rentedBooks.put(bookId, LocalDate.now());
    }

    public void removeRentedBook(String bookId) {
        rentedBooks.remove(bookId);
    }

    public Map<String, LocalDate> getRentedBooks() {
        return rentedBooks;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", rentedBooks=" + rentedBooks +
                ", penaltyPoints=" + penaltyPoints +
                ", blockedUntil=" + blockedUntil +
                '}';
    }
}
