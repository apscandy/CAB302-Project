package com.cab302.cab302project.error.deck;

public class DeckIsNullException extends RuntimeException {
    public DeckIsNullException() {}
    public DeckIsNullException(String message) {super(message);}
    public DeckIsNullException(Throwable cause) {super(cause);}
    public DeckIsNullException(String message, Throwable cause) {super(message, cause);}

}
