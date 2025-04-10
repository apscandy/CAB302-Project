package com.cab302.cab302project.error.model.question;

/**
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public class FailedToGetQuestionsException extends RuntimeException {
    public FailedToGetQuestionsException(){}
    public FailedToGetQuestionsException(String message) {super(message);}
    public FailedToGetQuestionsException(Throwable cause) {super(cause);}
    public FailedToGetQuestionsException(String message, Throwable cause) {super(message, cause);}
}
