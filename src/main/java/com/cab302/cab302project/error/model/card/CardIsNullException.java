package com.cab302.cab302project.error.model.card;

/**
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public class CardIsNullException extends  RuntimeException {
    public CardIsNullException() {}
    public CardIsNullException(String message) {super(message);}
    public CardIsNullException(Throwable cause) {super(cause);}
    public CardIsNullException(String message, Throwable cause) {super(message, cause);}

}
