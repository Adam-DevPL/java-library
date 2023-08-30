package org.example.service;

import org.example.Model.User;
import org.example.Model.UserDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    Optional<String> addUser(UserDto userDto);
    boolean removeUser(String userId);
    Optional<User> getUser(String userId);
    Optional<List<User>> getUsers();
    void borrowBook(String bookId, String userId);
    Integer returnBook(String bookId, String userId);
    void addPenaltyPoints(String userId, int penaltyPoints);
    Map<String, LocalDate> getBorrowedBooks(String userId);
}
