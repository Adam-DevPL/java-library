package org.example.service.Utils.exceptions;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
