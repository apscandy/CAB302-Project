package com.cab302.cab302project.error.model.card;

/**
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public class FailedToDeleteCardsException extends  RuntimeException {
    public FailedToDeleteCardsException() {}
    public FailedToDeleteCardsException(String message) {super(message);}
    public FailedToDeleteCardsException(Throwable cause) {super(cause);}
    public FailedToDeleteCardsException(String message, Throwable cause) {super(message, cause);}

}
