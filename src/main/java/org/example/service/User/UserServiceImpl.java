package org.example.service.User;

import org.example.Model.User.User;
import org.example.Model.User.UserDto;
import org.example.service.Utils.CustomException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final List<User> users = new ArrayList<>();

    @Override
    public String addUser(UserDto userDto) {
        Optional<User> user = findUser(userDto.name());

        if (user.isPresent()) {
            throw new CustomException("User already exists");
        }

        User newUser = new User(userDto);
        users.add(newUser);

        return newUser.getId();
    }

    @Override
    public boolean removeUser(String userId) {
        Optional<User> user = findUserById(userId);

        if (user.isEmpty()) {
            throw new CustomException("User does not exist");
        }

        users.remove(user.get());

        return true;
    }

    @Override
    public Optional<User> getUser(String userId) {
        Optional<User> user = findUserById(userId);

        if (user.isEmpty()) {
            throw new CustomException("User does not exist");
        }

        return user;
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    private Optional<User> findUser(String userName) {
        return users.stream()
                .filter(user -> user.getName().equals(userName))
                .findFirst();
    }

    private Optional<User> findUserById(String userId) {
        return users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst();
    }

}