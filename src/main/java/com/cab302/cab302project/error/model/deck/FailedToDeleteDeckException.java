package com.cab302.cab302project.error.model.deck;

/**
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public class FailedToDeleteDeckException extends RuntimeException {
    public FailedToDeleteDeckException() {}
    public FailedToDeleteDeckException(String message) {super(message);}
    public FailedToDeleteDeckException(Throwable cause) {super(cause);}
    public FailedToDeleteDeckException(String message, Throwable cause) {super(message, cause);}
}
