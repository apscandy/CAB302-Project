package com.cab302.cab302project.error.model.user;

public class FailedToUpdateUserException extends RuntimeException {
    public FailedToUpdateUserException(){}
    public FailedToUpdateUserException(String message) {super(message);}
    public FailedToUpdateUserException(Throwable cause) {super(cause);}
    public FailedToUpdateUserException(String message, Throwable cause) {super(message, cause);}
}
