package com.cab302.cab302project.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;

/**
 * This is a utility class for hashing user passwords using SHA-256
 * <p> This class provides a single static method, {@link #hashSHA256(String)},
 * which takes a plain-text password and returns its SHA-256 hexadecimal digest using
 * built-in {@link MessageDigest} for generating hash. No salt. </p>
 * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
 */
public class PasswordUtils {
    private static final Logger logger = LogManager.getLogger(PasswordUtils.class);
    public static String hashSHA256(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                String hexString = Integer.toHexString(0xff & b);
                if (hexString.length() == 1) hexString = "0" + hexString;
                hex.append(hexString);
            }
            return hex.toString();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
