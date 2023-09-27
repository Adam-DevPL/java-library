package org.example.service.Library;

import org.example.Model.Book.Book;
import org.example.Model.Book.BookDto;
import org.example.Model.Book.BookStock;
import org.example.Model.Booking.Booking;
import org.example.Model.User.User;
import org.example.Model.User.UserDto;
import org.example.service.Book.BookStockService;
import org.example.service.Booking.BookingService;
import org.example.service.User.UserService;
import org.example.service.Utils.CustomException;
import org.example.service.Utils.exceptions.LibraryException;
import org.example.service.Validation.Validation;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LibraryServiceImpl implements LibraryService {
    private final BookStockService bookStockService;
    private final UserService userService;
    private final BookingService bookingService;


    public LibraryServiceImpl(BookStockService bookStockService, UserService userService, BookingService bookingService) {
        this.bookStockService = bookStockService;
        this.userService = userService;
        this.bookingService = bookingService;
    }

    @Override
    public String addBook(BookDto bookDto) throws CustomException {
        try {
            Validation.isBookDtoValid(bookDto);
            return bookStockService.addBook(bookDto);
        } catch (CustomException e) {
            throw new LibraryException(e);
        }
    }

    @Override
    public boolean removeBook(String bookId) {
        try {
            return bookStockService.removeBook(bookId);
        } catch (CustomException e) {
            throw new LibraryException(e);
        }
    }

    @Override
    public String addUser(UserDto userDto) {
        try {
            Validation.isUserDtoValid(userDto);
            return userService.addUser(userDto);
        } catch (CustomException e) {
            throw new LibraryException(e);
        }
    }

    @Override
    public boolean removeUser(String userId) {
        try {
            return userService.removeUser(userId);
        } catch (CustomException e) {
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public void borrowBook(String bookId, String userId) {
        try {
            Optional<Book> book = bookStockService.getBook(bookId);

            if (book.isEmpty()) {
                throw new CustomException("Book does not exist");
            }

            Optional<User> user = userService.getUser(userId);

            if (user.isEmpty()) {
                throw new CustomException("User does not exist");
            }

            bookingService.borrowBook(bookId, userId);

        } catch (CustomException e) {
            throw new LibraryException(e);
        }
    }

    @Override
    public void returnBook(String bookId, String userId) {
        try {
            Optional<Book> book = bookStockService.getBook(bookId);

            if (book.isEmpty()) {
                throw new CustomException("Book does not exist");
            }

            Optional<User> user = userService.getUser(userId);

            if (user.isEmpty()) {
                throw new CustomException("User does not exist");
            }

            bookingService.returnBook(bookId, userId);

        } catch (CustomException e) {
            throw new LibraryException(e);
        }
    }

    @Override
    public List<BookStock> getBooksStock() {
        return bookStockService.getBooksStock();
    }

    @Override
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @Override
    public List<Booking> getBookings() {
        return bookingService.getBookings();
    }
}
