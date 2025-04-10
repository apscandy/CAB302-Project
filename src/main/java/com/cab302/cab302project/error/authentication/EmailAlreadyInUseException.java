package com.cab302.cab302project.error.authentication;

public class EmailAlreadyInUseException extends RuntimeException {
    public EmailAlreadyInUseException(String message) {super(message);}
    public EmailAlreadyInUseException(String message, Throwable cause) {super(message, cause);}
    public EmailAlreadyInUseException(Throwable cause) {super(cause);}
    public EmailAlreadyInUseException() {super();}
}
