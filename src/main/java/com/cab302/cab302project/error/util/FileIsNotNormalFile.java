package com.cab302.cab302project.error.util;

/**
 * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
 */
public class FileIsNotNormalFile extends RuntimeException {
    public FileIsNotNormalFile(String message) {
        super(message);
    }
}
