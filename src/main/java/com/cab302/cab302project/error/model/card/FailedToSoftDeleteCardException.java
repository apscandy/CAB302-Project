package com.cab302.cab302project.error.model.card;

public class FailedToSoftDeleteCardException extends RuntimeException {
    public FailedToSoftDeleteCardException(String message) {super(message);}
    public FailedToSoftDeleteCardException(String message, Throwable cause) {super(message, cause);}
    public FailedToSoftDeleteCardException(Throwable cause) {super(cause);}
    public FailedToSoftDeleteCardException() {}
}
