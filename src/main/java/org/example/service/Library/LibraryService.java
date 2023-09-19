package org.example.service.Library;

import org.example.Model.Book.BookDto;
import org.example.Model.Book.BookStock;
import org.example.Model.Booking.Booking;
import org.example.Model.User.User;
import org.example.Model.User.UserDto;
import org.example.service.Utils.CustomException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface LibraryService {
    String addBook(BookDto bookDto) throws CustomException;
    boolean removeBook(String bookId);
    String addUser(UserDto userDto);
    boolean removeUser(String userId);
    void borrowBook(String bookId, String userId);
    void returnBook(String bookId, String userId);
    List<BookStock> getBooksStock();
    List<User> getUsers();
    List<Booking> getBookings();
}
