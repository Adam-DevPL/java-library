package org.example.service.User;

import org.example.Model.User.User;
import org.example.Model.User.UserDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    String addUser(UserDto userDto);
    boolean removeUser(String userId);
    Optional<User> getUser(String userId);
    List<User> getUsers();
}
