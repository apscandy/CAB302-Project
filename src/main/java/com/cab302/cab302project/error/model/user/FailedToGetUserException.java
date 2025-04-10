package com.cab302.cab302project.error.model.user;

/**
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public class FailedToGetUserException extends RuntimeException {
    public FailedToGetUserException(){}
    public FailedToGetUserException(String message) {super(message);}
    public FailedToGetUserException(Throwable cause) {super(cause);}
    public FailedToGetUserException(String message, Throwable cause) {super(message, cause);}
}
