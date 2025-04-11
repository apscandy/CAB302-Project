package com.cab302.cab302project.error.model.deck;

/**
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public class DeckUserIdNullException extends RuntimeException {
    public DeckUserIdNullException() {}
    public DeckUserIdNullException(String message) {super(message);}
    public DeckUserIdNullException(Throwable cause) {super(cause);}
    public DeckUserIdNullException(String message, Throwable cause) {super(message, cause);}
}
