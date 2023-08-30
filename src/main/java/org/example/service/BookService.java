package org.example.service;

import org.example.Model.Book;
import org.example.Model.BookDto;
import org.example.Model.BookStock;

import java.util.List;
import java.util.Optional;

public interface BookService {
    String addBook(BookDto bookDto);
    boolean removeBook(String bookId);
    Optional<Book> getBook(String bookId);
    Optional<List<BookStock>> getBooksStock();
    void increaseQuantity(String bookId);
    void decreaseQuantity(String bookId);
}
