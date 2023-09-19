package org.example.service.Validation;

import org.example.Model.Book.BookDto;
import org.example.Model.User.UserDto;
import org.example.service.Utils.CustomException;

public class Validation {
    public static void isBookDtoValid(BookDto bookDto) throws CustomException {
        if (bookDto.title().length() == 0 || bookDto.author().length() == 0) {
            throw new CustomException("Book title or author cannot be empty");
        }
    }

    public static boolean isUserDtoValid(UserDto userDto) {
        return userDto.name().length() > 0;
    }

}
