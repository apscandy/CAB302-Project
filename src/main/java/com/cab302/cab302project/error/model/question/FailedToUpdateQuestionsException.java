package com.cab302.cab302project.error.model.question;

/**
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public class FailedToUpdateQuestionsException extends RuntimeException{
    public FailedToUpdateQuestionsException(){}
    public FailedToUpdateQuestionsException(Throwable cause) {super(cause);}
    public FailedToUpdateQuestionsException(String message) {super(message);}
    public FailedToUpdateQuestionsException(String message, Throwable cause) {super(message, cause);}
}
