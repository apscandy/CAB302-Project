package com.cab302.cab302project.error.model.question;

public class FailedToGetQuestionsException extends RuntimeException {
    public FailedToGetQuestionsException(){}
    public FailedToGetQuestionsException(String message) {super(message);}
    public FailedToGetQuestionsException(Throwable cause) {super(cause);}
    public FailedToGetQuestionsException(String message, Throwable cause) {super(message, cause);}
}
