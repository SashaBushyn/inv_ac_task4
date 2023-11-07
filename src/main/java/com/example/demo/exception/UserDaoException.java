package com.example.demo.exception;

public class UserDaoException extends RuntimeException {
    public UserDaoException(String message) {
        super(message);
    }

    public UserDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}