package org.example.service.Book;

import org.example.Model.Book.Book;
import org.example.Model.Book.BookDto;
import org.example.Model.Book.BookStock;

import java.util.List;
import java.util.Optional;

public interface BookStockService {
    String addBook(BookDto bookDto);
    boolean removeBook(String bookId);
    Optional<Book> getBook(String bookId);
    List<BookStock> getBooksStock();
    void increaseQuantity(String bookId);
    void decreaseQuantity(String bookId);
}
