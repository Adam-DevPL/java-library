package org.example.service;

import org.example.Model.Book;
import org.example.Model.User;
import org.example.Model.UserDto;

import java.time.LocalDate;
import java.util.*;

public class UserServiceImpl implements UserService {

    private final List<User> users = new ArrayList<>();

    @Override
    public Optional<String> addUser(UserDto userDto) {
        Optional<User> user = findUser(userDto.name());

        if (user.isPresent()) {
            return Optional.empty();
        }

        User newUser = new User(userDto);
        users.add(newUser);

        return Optional.of(newUser.getId());
    }

    @Override
    public boolean removeUser(String userId) {
        Optional<User> user = findUserById(userId);

        if (user.isEmpty()) {
            return false;
        }

        users.remove(user.get());

        return true;
    }

    @Override
    public Optional<User> getUser(String userId) {
        Optional<User> user = findUserById(userId);

        if (user.isEmpty()) {
            throw new IllegalArgumentException("User does not exist");
        }

        return user;
    }

    @Override
    public Optional<List<User>> getUsers() {
        return Optional.of(users);
    }

    @Override
    public void borrowBook(String bookId, String userId) {
        Optional<User> user = findUserById(userId);

        if (user.isEmpty()) {
            throw new IllegalArgumentException("User does not exist");
        }

        Optional<LocalDate> userBlockedDate = Optional.ofNullable(user.get().getBlockedUntil());

        if (userBlockedDate.isPresent() && userBlockedDate.get().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("User is blocked");
        }

        Optional<Map<String, LocalDate>> userRentedBooks = Optional.ofNullable(user.get().getRentedBooks());

        boolean rentedBook = userRentedBooks.isPresent() && userRentedBooks.get().containsKey(bookId);

        if (rentedBook) {
            throw new IllegalArgumentException("User already rented this book");
        }

        user.get().addRentedBook(bookId);
    }

    @Override
    public Integer returnBook(String bookId, String userId) {
        Optional<User> user = findUserById(userId);

        if (user.isEmpty()) {
            throw new IllegalArgumentException("User does not exist");
        }

        Optional<Map<String, LocalDate>> userRentedBooks = Optional.ofNullable(user.get().getRentedBooks());

        if (userRentedBooks.isEmpty()) {
            throw new IllegalArgumentException("User did not rent any books");
        }


        Optional<Map<String, LocalDate>> rentedBook = userRentedBooks
                .filter(books -> books.containsKey(bookId));


        if (rentedBook.isEmpty()) {
            throw new IllegalArgumentException("User did not rent this book");
        }

        Integer penaltyPoints = rentedBook.get().get(bookId).until(LocalDate.now()).getDays();

        user.get().removeRentedBook(bookId);

        return penaltyPoints;
    }

    @Override
    public void addPenaltyPoints(String userId, int penaltyPoints) {
        Optional<User> user = findUserById(userId);

        if (user.isEmpty()) {
            throw new IllegalArgumentException("User does not exist");
        }

        int userPenaltyPoints = user.get().getPenaltyPoints() + penaltyPoints;
        user.get().setPenaltyPoints(userPenaltyPoints);

        if (userPenaltyPoints > 10) {
            user.get().setBlockedUntil(LocalDate.now().plusDays(30));
        }
    }

    @Override
    public Map<String, LocalDate> getBorrowedBooks(String userId) {
        Optional<User> user = findUserById(userId);

        if (user.isEmpty()) {
            throw new IllegalArgumentException("User does not exist");
        }

        return user.get().getRentedBooks();
    }

    private Optional<User> findUser(String userName) {
        return users.stream()
                .filter(user -> user.getName().equals(userName))
                .findFirst();
    }

    private Optional<User> findUserById(String userId) {
        return users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst();
    }
}
