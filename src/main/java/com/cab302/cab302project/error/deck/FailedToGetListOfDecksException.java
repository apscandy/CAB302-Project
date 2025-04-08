package com.cab302.cab302project.error.deck;

public class FailedToGetListOfDecksException extends RuntimeException {
    public FailedToGetListOfDecksException() {}
    public FailedToGetListOfDecksException(String message) {}
    public FailedToGetListOfDecksException(Throwable cause) { super(cause); }
    public FailedToGetListOfDecksException(String message, Throwable cause) { super(message, cause); }
}
