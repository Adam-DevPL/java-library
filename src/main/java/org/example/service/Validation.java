package org.example.service;

import org.example.Model.BookDto;
import org.example.Model.UserDto;

public class Validation {
    public static boolean isBookDtoValid(BookDto bookDto) {
        return bookDto.title().length() > 0 && bookDto.author().length() > 0;
    }

    public static boolean isUserDtoValid(UserDto userDto) {
        return userDto.name().length() > 0;
    }

}
