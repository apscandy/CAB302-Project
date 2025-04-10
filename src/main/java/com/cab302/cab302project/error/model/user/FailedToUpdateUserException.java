package com.cab302.cab302project.error.model.user;

/**
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public class FailedToUpdateUserException extends RuntimeException {
    public FailedToUpdateUserException(){}
    public FailedToUpdateUserException(String message) {super(message);}
    public FailedToUpdateUserException(Throwable cause) {super(cause);}
    public FailedToUpdateUserException(String message, Throwable cause) {super(message, cause);}
}
