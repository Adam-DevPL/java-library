package org.example.service;

import org.example.Model.User;
import org.example.Model.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl();
    }

    @Test
    @DisplayName("Add user")
    void addUser() {
        //given
        UserDto userDto = new UserDto("name");

        //when
        Optional<String> userId = userService.addUser(userDto);
        Optional<List<User>> users = userService.getUsers();

        //then
        assertNotNull(userId);
        assertTrue(users.isPresent());
        assertEquals(1, users.get().size());
        assertEquals(userId.get(), users.get().get(0).getId());
    }

    @Test
    @DisplayName("Add user - multiple users")
    void addUser_multipleUsers() {
        //given
        UserDto userDto = new UserDto("name");
        UserDto userDto2 = new UserDto("name");

        //when
        Optional<String> userId = userService.addUser(userDto);
        Optional<String> userId2 = userService.addUser(userDto2);

        //then
        assertNotNull(userId);
        assertEquals(userId2, Optional.empty());
    }

    @Test
    @DisplayName("Remove user")
    void removeUser() {
        //given
        UserDto userDto = new UserDto("name");
        Optional<String> userId = userService.addUser(userDto);

        //when
        if (userId.isEmpty()) {
            fail();
        }
        boolean isRemoved = userService.removeUser(userId.get());
        Optional<List<User>> users = userService.getUsers();

        //then
        assertTrue(isRemoved);
        assertTrue(users.isPresent());
        assertEquals(0, users.get().size());
    }

    @Test
    @DisplayName("remove not existing user")
    void removeUser_notExistingUser() {
        boolean isRemoved = userService.removeUser("notExistingId");

        //then
        assertFalse(isRemoved);
    }

    @Test
    @DisplayName("Get user")
    void getUser() {
        //given
        UserDto userDto = new UserDto("name");
        Optional<String> userId = userService.addUser(userDto);

        //when
        if (userId.isEmpty()) {
            fail();
        }
        Optional<User> user = userService.getUser(userId.get());

        //then
        assertTrue(user.isPresent());
        assertEquals(userId.get(), user.get().getId());
    }

    @Test
    @DisplayName("Get user - not found")
    void getUser_notFound() {
        //given
        String fakeUserId = "fakeUserId";

        //when
        assertThrows(IllegalArgumentException.class, () -> userService.getUser(fakeUserId));
    }

    @Test
    @DisplayName("Get users")
    void getUsers() {
        //given
        UserDto userDto = new UserDto("name");
        Optional<String> userId = userService.addUser(userDto);

        //when
        if (userId.isEmpty()) {
            fail();
        }
        Optional<List<User>> users = userService.getUsers();

        //then
        assertTrue(users.isPresent());
        assertEquals(1, users.get().size());
        assertEquals(userId.get(), users.get().get(0).getId());
    }

    @Test
    @DisplayName("Get users - empty")
    void getUsers_empty() {
        //when
        Optional<List<User>> users = userService.getUsers();

        //then
        assertTrue(users.isPresent());
        assertEquals(0, users.get().size());
    }

    @Test
    @DisplayName("Borrow book")
    void borrowBook() {
        //given
        UserDto userDto = new UserDto("name");
        Optional<String> userId = userService.addUser(userDto);
        String bookId = "bookId";

        //when
        if (userId.isEmpty()) {
            fail();
        }
        userService.borrowBook(bookId, userId.get());
        Optional<User> user = userService.getUser(userId.get());
        if (user.isEmpty()) {
            fail();
        }
        Map<String, LocalDate> rentedBooks = user.get().getRentedBooks();

        //then
        assertEquals(1, rentedBooks.size());
        assertEquals(bookId, rentedBooks.keySet().toArray()[0]);
    }

    @Test
    @DisplayName("Borrow book - user not found")
    void borrowBook_userNotFound() {
        //when
        assertThrows(IllegalArgumentException.class, () -> userService.borrowBook("bookId", "notFound"));
    }

    @Test
    @DisplayName("Borrow book - user is blocked")
    void borrowBook_userIsBlocked() {
        //given
        UserDto userDto = new UserDto("name");
        Optional<String> userId = userService.addUser(userDto);
        String bookId = "bookId";
        if (userId.isEmpty()) {
            fail();
        }
        userService.addPenaltyPoints(userId.get(), 15);

        //then
        assertThrows(IllegalArgumentException.class, () -> userService.borrowBook(bookId, userId.get()));
    }

    @Test
    @DisplayName("Borrow book - user already borrowed book")
    void borrowBook_userAlreadyBorrowedBook() {
        //given
        UserDto userDto = new UserDto("name");
        Optional<String> userId = userService.addUser(userDto);
        String bookId = "bookId";
        if (userId.isEmpty()) {
            fail();
        }
        userService.borrowBook(bookId, userId.get());

        //then
        assertThrows(IllegalArgumentException.class, () -> userService.borrowBook(bookId, userId.get()));
    }

    @Test
    @DisplayName("Return book")
    void returnBook() {
        //given
        UserDto userDto = new UserDto("name");
        Optional<String> userId = userService.addUser(userDto);
        String bookId = "bookId";
        if (userId.isEmpty()) {
            fail();
        }
        userService.borrowBook(bookId, userId.get());

        //when
        Integer days = userService.returnBook(bookId, userId.get());
        Optional<User> user = userService.getUser(userId.get());
        if (user.isEmpty()) {
            fail();
        }
        Map<String, LocalDate> rentedBooks = user.get().getRentedBooks();

        //then
        assertEquals(0, rentedBooks.size());
        assertEquals(0, days);
    }

    @Test
    @DisplayName("Return book - user not found")
    void returnBook_userNotFound() {
        //when
        assertThrows(IllegalArgumentException.class, () -> userService.returnBook("bookId", "notFound"));
    }

    @Test
    @DisplayName("Return book - user did not borrow book")
    void returnBook_userDidNotBorrowBook() {
        //given
        UserDto userDto = new UserDto("name");
        Optional<String> userId = userService.addUser(userDto);
        String bookId = "bookId";
        if (userId.isEmpty()) {
            fail();
        }

        //then
        assertThrows(IllegalArgumentException.class, () -> userService.returnBook(bookId, userId.get()));
    }
}