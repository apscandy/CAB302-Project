package com.cab302.cab302project.error;

public class PasswordIsNullException extends RuntimeException{
    
    public PasswordIsNullException(){}
    public PasswordIsNullException(String message) {
        super(message);
    }
    public PasswordIsNullException(Throwable cause) {super(cause);}
    public PasswordIsNullException(String message, Throwable cause) {super(message, cause);}

}