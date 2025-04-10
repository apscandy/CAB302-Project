package com.cab302.cab302project.error.authentication;


/**
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public class EmptyAnswerException extends RuntimeException {
    public EmptyAnswerException() {}
    public EmptyAnswerException(String message) {}
    public EmptyAnswerException(Throwable cause) { super(cause); }
    public EmptyAnswerException(String message, Throwable cause) {}
}
