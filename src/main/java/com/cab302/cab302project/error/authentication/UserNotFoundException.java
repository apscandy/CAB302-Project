package com.cab302.cab302project.error.authentication;

/**
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {}
    public UserNotFoundException(String message) {}
    public UserNotFoundException(Throwable cause) {}
    public UserNotFoundException(String message, Throwable cause) {}
}
