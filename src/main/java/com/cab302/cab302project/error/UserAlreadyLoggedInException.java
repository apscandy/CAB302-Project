package com.cab302.cab302project.error;

/**
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public class UserAlreadyLoggedInException extends RuntimeException {
    public UserAlreadyLoggedInException() {}
    public UserAlreadyLoggedInException(String message) {}
    public UserAlreadyLoggedInException(Throwable cause) {}
    public UserAlreadyLoggedInException(String message, Throwable cause) {}
}
