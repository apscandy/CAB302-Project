package com.cab302.cab302project.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegexValidatorTest {

    @Test
    void validEmailAddress() {
        assertFalse(RegexValidator.validEmailAddress("plainaddress"));
        assertFalse(RegexValidator.validEmailAddress("@missingusername.com"));
        assertFalse(RegexValidator.validEmailAddress("missingdomain@.com"));
        assertFalse(RegexValidator.validEmailAddress("missingat.com"));
        assertFalse(RegexValidator.validEmailAddress("user@domain"));
        assertFalse(RegexValidator.validEmailAddress("user@domain@domain.com"));
        assertFalse(RegexValidator.validEmailAddress("user@.com"));
        assertFalse(RegexValidator.validEmailAddress("user@domain..com"));
        assertFalse(RegexValidator.validEmailAddress("user@domain!com"));
        assertFalse(RegexValidator.validEmailAddress("user@domain#.com"));
        assertFalse(RegexValidator.validEmailAddress("user@domain.com."));
        assertFalse(RegexValidator.validEmailAddress("us..er@domain.com"));
        assertFalse(RegexValidator.validEmailAddress("user@doma_in.com"));
        assertFalse(RegexValidator.validEmailAddress("user@domain_com"));
        assertFalse(RegexValidator.validEmailAddress("user@domain#com"));
        assertFalse(RegexValidator.validEmailAddress("user@domaincom."));
        assertFalse(RegexValidator.validEmailAddress("user@domain.c"));
        assertFalse(RegexValidator.validEmailAddress("user@domain....com"));
        assertFalse(RegexValidator.validEmailAddress("user@com"));
        assertFalse(RegexValidator.validEmailAddress("user@-domain.com"));
        assertFalse(RegexValidator.validEmailAddress("user@domain-.com"));
        assertFalse(RegexValidator.validEmailAddress("us..er@domain.com"));
        assertFalse(RegexValidator.validEmailAddress(".user@domain.com"));
        assertFalse(RegexValidator.validEmailAddress("user.@domain.com"));
        assertFalse(RegexValidator.validEmailAddress("us!er@domain.com"));
//        assertTrue(RegexValidator.validEmailAddress("johnwick@gmail.com"));
//        assertTrue(RegexValidator.validEmailAddress("maverickdoan@qut.edu.au"));
    }

    @Test
    void validPassword() {
        assertFalse(RegexValidator.validPassword("abc123"));
        assertFalse(RegexValidator.validPassword("1234"));
        assertFalse(RegexValidator.validPassword("password"));
        assertFalse(RegexValidator.validPassword("12345"));
        assertFalse(RegexValidator.validPassword("admin"));
        assertFalse(RegexValidator.validPassword("qwerty"));
        assertFalse(RegexValidator.validPassword("hello"));
        assertFalse(RegexValidator.validPassword("123"));
        assertFalse(RegexValidator.validPassword("welcome"));
        assertFalse(RegexValidator.validPassword("summer"));
        assertFalse(RegexValidator.validPassword("1qaz"));
        assertFalse(RegexValidator.validPassword("abc"));
        assertFalse(RegexValidator.validPassword("00000"));
        assertFalse(RegexValidator.validPassword("love"));
        assertFalse(RegexValidator.validPassword("test"));
        assertFalse(RegexValidator.validPassword("987654"));
        assertFalse(RegexValidator.validPassword("monkey"));
        assertFalse(RegexValidator.validPassword("apple"));
        assertFalse(RegexValidator.validPassword("secret"));
        assertFalse(RegexValidator.validPassword("sunshine"));
        assertFalse(RegexValidator.validPassword("password1"));
        assertFalse(RegexValidator.validPassword("123qwe"));
        assertFalse(RegexValidator.validPassword("qwertyui"));
        assertFalse(RegexValidator.validPassword("123asd"));
    }
}