package com.cab302.cab302project.error.model.card;

public class FailedToUpdateCardException extends RuntimeException {
    public FailedToUpdateCardException() {}
    public FailedToUpdateCardException(String message) {super(message);}
    public FailedToUpdateCardException(Throwable cause) {super(cause);}
    public FailedToUpdateCardException(Throwable cause, String message) {super(message, cause);}
}
