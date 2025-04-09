package com.cab302.cab302project.error.deck;

public class FailedToGetDeckException extends RuntimeException {
    public FailedToGetDeckException() {}
    public FailedToGetDeckException(String message) {super(message);}
    public FailedToGetDeckException(Throwable cause) {super(cause);}
    public FailedToGetDeckException(String message, Throwable cause) {super(message, cause);}
}
