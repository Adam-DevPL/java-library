package org.example.service;

import org.example.Model.Book;
import org.example.Model.BookDto;
import org.example.Model.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryServiceImpl implements LibraryService {
    private final List<User> users;
    private final Map<Book, Integer> books;
    private final Map<Book, LocalDate> rentedBooks;

    public LibraryServiceImpl() {
        this.users = new ArrayList<>();
        this.books = new HashMap<>();
        this.rentedBooks = new HashMap<>();
    }

    public String addBook(BookDto bookDto) {
        Book book = books.keySet().stream()
                .filter(b -> b.getTitle().equals(bookDto.getTitle()) && b.getAuthor().equals(bookDto.getAuthor()))
                .findFirst()
                .orElse(null);

        if (book != null) {
            books.put(book, books.get(book) + 1);
        } else {
            book = new Book(bookDto);
            books.put(book, 1);
        }

        return book.getId();
    }

    public String addUser(String name) {
        User user = users.stream()
                .filter(u -> u.getName().equals(name))
                .findFirst()
                .orElse(null);

        if (user != null) {
            throw new IllegalArgumentException("User already exists");
        }

        User newUser = new User(name);
        users.add(newUser);

        return newUser.getId();
    }

    public void removeBook(String bookId) {
        Book book = books.keySet().stream()
                .filter(b -> b.getId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));


        if (books.get(book) > 1) {
            books.put(book, books.get(book) - 1);
            return;
        }

        books.remove(book);
    }

    public void removeUser(String userId) {
        User user = users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        users.remove(user);
    }

    public String rentBook(String bookId, String userId) {
        User user = users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        if (user.hasBeenBlocked()) {
            throw new IllegalArgumentException("User has been blocked");
        }


        if (user.getRentedBooks().stream().filter(b -> b.equals(bookId)).findFirst().orElse(null) != null) {
            throw new IllegalArgumentException("User is already renting a book");
        }


        Book bookToRent = books.keySet().stream()
                .filter(b -> b.getId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));


        if (books.get(bookToRent) == 0) {
            throw new IllegalArgumentException("Book is not available");
        }

        user.addBook(bookToRent.getId());
        rentedBooks.put(bookToRent, LocalDate.now().plusDays(7));

        books.put(bookToRent, books.get(bookToRent) - 1);

        return bookToRent.getId();
    }

    public void returnBook(String bookId, String userId) {
        User user = users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Book rentedBook = rentedBooks.keySet().stream()
                .filter(b -> b.getId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));


        String userBookId = user.getRentedBooks().stream()
                .filter(b -> b.equals(bookId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User is not renting a book"));


        int daysAfterDueDate = (int) ChronoUnit.DAYS.between(rentedBooks.get(rentedBook), LocalDate.now());

        user.addPenaltyPoints(daysAfterDueDate);

        user.removeBook(userBookId);

        rentedBooks.remove(rentedBook);

        books.put(rentedBook, books.get(rentedBook) + 1);
    }

    public Map<Book, Integer> getBooks() {
        return books;
    }

    public List<User> getUsers() {
        return users;
    }

    public Map<Book, LocalDate> getRentedBooks() {
        return rentedBooks;
    }

    public Book getBook(String bookId) {
        return books.keySet().stream()
                .filter(b -> b.getId().equals(bookId))
                .findFirst()
                .orElse(null);
    }

    public Book getRentedBook(String bookId) {
        return rentedBooks.keySet().stream()
                .filter(b -> b.getId().equals(bookId))
                .findFirst()
                .orElse(null);
    }

    public User getUser(String userId) {
        return users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    public void setBookDueDate(String bookId, LocalDate date) {
        Book rentedBook = rentedBooks.keySet().stream()
                .filter(b -> b.getId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        rentedBooks.put(rentedBook, date);
    }
}
