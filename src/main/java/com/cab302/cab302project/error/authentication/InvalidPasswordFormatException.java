package com.cab302.cab302project.error.authentication;

/**
 * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
 */
public class InvalidPasswordFormatException extends RuntimeException {
    public InvalidPasswordFormatException(String message) {
        super(message);
    }
}
