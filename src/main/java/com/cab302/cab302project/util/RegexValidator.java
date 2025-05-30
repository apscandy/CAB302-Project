package com.cab302.cab302project.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Utility class that holds static methods to validate items.
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public class RegexValidator {


    /**
     * Simple function email goes in and boolean comes out.
     * This cannot guarantee it will get all email address correct, since
     * email addresses are complex and have many different formats.
     * <p>
     * If you make any modification to the regex please run the tests
     * <p>
     * I looked up implementations of regex rather than building it
     * because there are much smarter people then me that spent
     * the time and effort to make a regex patten.
     * <a href="https://www.baeldung.com/java-email-validation-regex">Regex found here</a>
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     * @param emailAddress email to be validated
     * @return true if the email is a valid email address otherwise returns false
     */
    public static boolean validEmailAddress(String emailAddress) {
        var patternRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.matches(patternRegex, emailAddress);
    }

    /**
     * Simple function password goes in and boolean comes out.
     * Password must have minimum 8 characters with at lest one uppercase,
     * one lowercase, one digit and one special charter
     * <p>
     * If you make any modification to the regex please run the tests
     * <p>
     * I looked up implementations of regex rather than building it
     * because there are much smarter people then me that spent
     * the time and effort to make a regex patten.
     * <a href="https://uibakery.io/regex-library/password-regex-java">Regex found here</a>
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     * @param password password to be validated
     * @return true if the password meet requirements else returns false
     */
    public static boolean validPassword(String password) {
        var patternRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
        return Pattern.matches(patternRegex, password);
    }
}
