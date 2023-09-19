package org.example.service.Book;

import org.example.Model.Book.Book;
import org.example.Model.Book.BookDto;
import org.example.Model.Book.BookStock;
import org.example.service.Utils.CustomException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookStockServiceImpl implements BookStockService {

    private final List<BookStock> bookStocks = new ArrayList<>();

    @Override
    public String addBook(BookDto bookDto) {
        final Optional<BookStock> bookStock = findBook(bookDto.title(), bookDto.author());

        if (bookStock.isPresent()) {
            bookStock.get().increaseQuantity();
            return bookStock.get().getBook().getId();
        }
        Book newBook = new Book(bookDto);
        BookStock newBookStock = new BookStock(newBook);

        bookStocks.add(newBookStock);

        return newBook.getId();
    }


    @Override
    public boolean removeBook(String bookId) {
        Optional<BookStock> bookStock = findBook(bookId);

        if (bookStock.isEmpty()) {
            throw new CustomException("Book does not exist");
        }

        if (bookStock.get().getQuantity() > 1) {
            bookStock.get().decreaseQuantity();
        } else {
            bookStocks.remove(bookStock.get());
        }

        return true;
    }

    @Override
    public Optional<Book> getBook(String bookId) {
        Optional<BookStock> bookStock = findBook(bookId);

        if (bookStock.isEmpty()) {
            throw new CustomException("Book does not exist");
        }

        if (bookStock.get().getQuantity() == 0) {
            throw new CustomException("Book is out of stock");
        }

        return bookStock.map(BookStock::getBook);
    }

    @Override
    public List<BookStock> getBooksStock() {
        return bookStocks;
    }

    @Override
    public void increaseQuantity(String bookId) {
        Optional<BookStock> bookStock = findBook(bookId);

        if (bookStock.isEmpty()) {
            throw new CustomException("Book does not exist");
        }

        bookStock.get().increaseQuantity();
    }

    @Override
    public void decreaseQuantity(String bookId) {
        Optional<BookStock> bookStock = findBook(bookId);

        if (bookStock.isEmpty()) {
            throw new CustomException("Book does not exist");
        }


        bookStock.get().decreaseQuantity();
    }

    private Optional<BookStock> findBook(String bookTitle, String bookAuthor) {
        return bookStocks.stream()
                .filter(bookStock -> bookStock.getBook().getAuthor().equals(bookAuthor) &&
                        bookStock.getBook().getTitle().equals(bookTitle))
                .findFirst();
    }

    private Optional<BookStock> findBook(String bookId) {
        return bookStocks.stream()
                .filter(bookStock -> bookStock.getBook().getId().equals(bookId))
                .findFirst();
    }
}
