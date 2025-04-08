package com.cab302.cab302project.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexValidator {

    // https://uibakery.io/regex-library/email-regex-java
    public static boolean validEmailAddress(String emailAddress) {
        Pattern pattern = Pattern.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");
        Matcher target = pattern.matcher(emailAddress);
        return target.find();
    }

    // https://uibakery.io/regex-library/email-regex-java
    public static boolean validPassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
        Matcher target = pattern.matcher(password);
        return target.find();
    }
}
