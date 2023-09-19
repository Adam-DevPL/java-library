package org.example.service.User;

import org.example.Model.User.UserDto;
import org.example.service.Utils.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl();
    }

    @Test
    @DisplayName("Test for addUser")
    void addUser() {
        // given
        UserDto userDto = new UserDto("name");

        // when
        String newUserId = userService.addUser(userDto);

        // then
        assertEquals(1, userService.getUsers().size());
        assertEquals(newUserId, userService.getUsers().get(0).getId());
        assertEquals("name", userService.getUsers().get(0).getName());
    }

    @Test
    @DisplayName("Test for removeUser")
    void removeUser() {
        // given
        UserDto userDto = new UserDto("name");
        String newUserId = userService.addUser(userDto);

        // when
        boolean isRemoved = userService.removeUser(newUserId);

        // then
        assertTrue(isRemoved);
        assertEquals(0, userService.getUsers().size());
    }

    @Test
    @DisplayName("Test for getUser")
    void getUser() {
        // given
        UserDto userDto = new UserDto("name");
        String newUserId = userService.addUser(userDto);

        // when
        String userId = userService.getUser(newUserId).get().getId();

        // then
        assertEquals(newUserId, userId);
    }

    @Test
    @DisplayName("Test for getUser when user does not exist")
    void getUserWhenUserDoesNotExist() {
        // then
        assertThrowsExactly(CustomException.class, () -> userService.getUser("wrongId"), "User does not exist");
    }
}