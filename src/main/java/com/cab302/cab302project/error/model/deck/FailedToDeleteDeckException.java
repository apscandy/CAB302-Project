package com.cab302.cab302project.error.model.deck;

public class FailedToDeleteDeckException extends RuntimeException {
    public FailedToDeleteDeckException() {}
    public FailedToDeleteDeckException(String message) {super(message);}
    public FailedToDeleteDeckException(Throwable cause) {super(cause);}
    public FailedToDeleteDeckException(String message, Throwable cause) {super(message, cause);}
}
