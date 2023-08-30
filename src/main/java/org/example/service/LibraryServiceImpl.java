package org.example.service;

import org.example.Model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LibraryServiceImpl implements LibraryService {
    private final BookService bookService;
    private final UserService userService;


    public LibraryServiceImpl(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    @Override
    public String addBook(BookDto bookDto) {
        if (!Validation.isBookDtoValid(bookDto)) {
            throw new IllegalArgumentException("Book is not valid");
        }
        return bookService.addBook(bookDto);
    }

    @Override
    public boolean removeBook(String bookId) {
        boolean isSuccess = bookService.removeBook(bookId);

        if (!isSuccess) {
            throw new IllegalArgumentException("Book not found");
        }

        return true;
    }

    @Override
    public String addUser(UserDto userDto) {
        if (!Validation.isUserDtoValid(userDto)) {
            throw new IllegalArgumentException("User is not valid");
        }

        Optional<String> newUserId = userService.addUser(userDto);

        if (newUserId.isEmpty()) {
            throw new IllegalArgumentException("User already exists");
        }

        return newUserId.get();
    }

    @Override
    public boolean removeUser(String userId) {
        boolean isSuccess = userService.removeUser(userId);

        if (!isSuccess) {
            throw new IllegalArgumentException("User not found");
        }

        return true;
    }

    @Override
    public void borrowBook(String bookId, String userId) {
        userService.borrowBook(bookId, userId);
        bookService.decreaseQuantity(bookId);
    }

    @Override
    public void returnBook(String bookId, String userId) {
        Integer days = userService.returnBook(bookId, userId);

        if (days > 10) {
            userService.addPenaltyPoints(userId, days);
        }

        bookService.increaseQuantity(bookId);
    }

    @Override
    public List<BookStock> getBooksStock() {
        Optional<List<BookStock>> booksStock = bookService.getBooksStock();

        if (booksStock.isEmpty()) {
            throw new IllegalArgumentException("No books in stock");
        }

        return booksStock.get();
    }

    @Override
    public List<User> getUsers() {
        Optional<List<User>> users = userService.getUsers();

        if (users.isEmpty()) {
            throw new IllegalArgumentException("No users");
        }

        return users.get();
    }

    @Override
    public Map<String, LocalDate> getBorrowedBooks(String userId) {
        return userService.getBorrowedBooks(userId);
    }
}
