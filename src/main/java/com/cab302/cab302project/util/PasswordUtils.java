package com.cab302.cab302project.util;

import com.cab302.cab302project.controller.user.UserController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;

public class PasswordUtils {
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
            throw new RuntimeException(e);
        }
    }
}
