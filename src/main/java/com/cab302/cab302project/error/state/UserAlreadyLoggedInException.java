package com.cab302.cab302project.error.state;

/**
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public class UserAlreadyLoggedInException extends RuntimeException {
    public UserAlreadyLoggedInException() {}
    public UserAlreadyLoggedInException(String message) {super(message);}
    public UserAlreadyLoggedInException(Throwable cause) {super(cause);}
    public UserAlreadyLoggedInException(String message, Throwable cause) {super(message, cause);}
}
