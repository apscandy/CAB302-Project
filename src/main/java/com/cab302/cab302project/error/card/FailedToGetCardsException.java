package com.cab302.cab302project.error.card;

public class FailedToGetCardsException extends  RuntimeException {
    public FailedToGetCardsException() {}
    public FailedToGetCardsException(String message) {super(message);}
    public FailedToGetCardsException(Throwable cause) {super(cause);}
    public FailedToGetCardsException(String message, Throwable cause) {super(message, cause);}

}
