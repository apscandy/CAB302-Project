package com.cab302.cab302project.error.deck;

public class FailedToUpdateDeckException extends RuntimeException {
    public FailedToUpdateDeckException() {}
    public FailedToUpdateDeckException(String message) {super(message);}
    public FailedToUpdateDeckException(Throwable cause) { super(cause); }
    public FailedToUpdateDeckException(String message, Throwable cause) {super(message, cause);}
}
