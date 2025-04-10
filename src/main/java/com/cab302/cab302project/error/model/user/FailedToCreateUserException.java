package com.cab302.cab302project.error.model.user;

public class FailedToCreateUserException extends RuntimeException{
    public FailedToCreateUserException(){}
    public FailedToCreateUserException(String message) {super(message);}
    public FailedToCreateUserException(Throwable cause) {super(cause);}
    public FailedToCreateUserException(String message, Throwable cause) {super(message, cause);}
}
