package com.cab302.cab302project.error.model.deck;

/**
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public class FailedToUpdateDeckException extends RuntimeException {
    public FailedToUpdateDeckException() {}
    public FailedToUpdateDeckException(String message) {super(message);}
    public FailedToUpdateDeckException(Throwable cause) { super(cause); }
    public FailedToUpdateDeckException(String message, Throwable cause) {super(message, cause);}
}
