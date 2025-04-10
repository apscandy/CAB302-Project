package com.cab302.cab302project.error.model.deck;

public class FailedToGetListOfDecksException extends RuntimeException {
    public FailedToGetListOfDecksException() {}
    public FailedToGetListOfDecksException(String message) {super();}
    public FailedToGetListOfDecksException(Throwable cause) { super(cause); }
    public FailedToGetListOfDecksException(String message, Throwable cause) { super(message, cause); }
}
