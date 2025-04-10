package com.cab302.cab302project.error.state;

/**
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public class UserIsNullException extends RuntimeException {
    public UserIsNullException() {}
    public UserIsNullException(String message) {super(message);}
    public UserIsNullException(Throwable cause) {super(cause);}
    public UserIsNullException(String message, Throwable cause) {super(message, cause);}
}
