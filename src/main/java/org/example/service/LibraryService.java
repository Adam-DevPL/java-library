package org.example.service;

import org.example.Model.Book;
import org.example.Model.BookDto;
import org.example.Model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface LibraryService {
    String addBook(BookDto bookDto);

    String addUser(String name);

    void removeBook(String bookId);

    void removeUser(String userId);

    String rentBook(String bookId, String userId);

    void returnBook(String bookId, String userId);

    Map<Book, Integer> getBooks();

    List<User> getUsers();

    Map<Book, LocalDate> getRentedBooks();

    Book getBook(String bookId);

    Book getRentedBook(String bookId);

    User getUser(String userId);

    void setBookDueDate(String bookId, LocalDate date);
}
