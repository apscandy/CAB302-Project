package com.cab302.cab302project.error.model.question;

/**
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public class FailedToCreateQuestionsException extends RuntimeException {
    public FailedToCreateQuestionsException(){}
    public FailedToCreateQuestionsException(String message) {super(message);}
    public FailedToCreateQuestionsException(Throwable cause) {super(cause);}
    public FailedToCreateQuestionsException(String message, Throwable cause) {super(message, cause);}

}
