package org.example.Model.User;

import java.time.LocalDate;
import java.util.*;

public class User {
    private final String id;
    private final String name;


    public User(UserDto userDto) {
        this.id = UUID.randomUUID().toString();
        this.name = userDto.name();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
