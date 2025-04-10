package com.cab302.cab302project.error.authentication;

/**
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public class InvalidEmailFormatException extends RuntimeException {
    public InvalidEmailFormatException(String message) {
        super(message);
    }
}
