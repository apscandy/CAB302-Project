package com.cab302.cab302project.error.model.deck;

public class FailedToCreateDeckException extends RuntimeException {
    public FailedToCreateDeckException() {}
    public FailedToCreateDeckException(String message) {super(message);}
    public FailedToCreateDeckException(Throwable cause) {super(cause);}
    public FailedToCreateDeckException(String message, Throwable cause) {super(message, cause);}
}
