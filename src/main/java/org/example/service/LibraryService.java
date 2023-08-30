package org.example.service;

import org.example.Model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface LibraryService {
    String addBook(BookDto bookDto);
    boolean removeBook(String bookId);
    String addUser(UserDto userDto);
    boolean removeUser(String userId);
    void borrowBook(String bookId, String userId);
    void returnBook(String bookId, String userId);

    List<BookStock> getBooksStock();
    List<User> getUsers();
    Map<String, LocalDate> getBorrowedBooks(String userId);
}
