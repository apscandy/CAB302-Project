package com.cab302.cab302project.error.model.user;

/**
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public class FailedToCreateUserException extends RuntimeException{
    public FailedToCreateUserException(){}
    public FailedToCreateUserException(String message) {super(message);}
    public FailedToCreateUserException(Throwable cause) {super(cause);}
    public FailedToCreateUserException(String message, Throwable cause) {super(message, cause);}
}
