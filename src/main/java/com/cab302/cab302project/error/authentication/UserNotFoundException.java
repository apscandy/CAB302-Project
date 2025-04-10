package com.cab302.cab302project.error.authentication;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {}
    public UserNotFoundException(String message) {}
    public UserNotFoundException(Throwable cause) {}
    public UserNotFoundException(String message, Throwable cause) {}
}
