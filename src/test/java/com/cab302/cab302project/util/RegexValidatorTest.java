package com.cab302.cab302project.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegexValidatorTest {

    @Test
    void validEmailAddress1() {
        assertTrue(RegexValidator.validEmailAddress("johnwick@gmail.com"));
    }

    @Test
    void validEmailAddress2() {
        assertTrue(RegexValidator.validEmailAddress("maverickdoan@qut.edu.au"));
    }

    @Test
    void validEmailAddress3() {
        assertTrue(RegexValidator.validEmailAddress("johndoe123@example.com"));
    }

    @Test
    void validEmailAddress4() {
        assertTrue(RegexValidator.validEmailAddress("sarah.smith@testingmail.com"));
    }

    @Test
    void validEmailAddress5() {
        assertTrue(RegexValidator.validEmailAddress("mike.jones@domain.org"));
    }

    @Test
    void validEmailAddress6() {
        assertTrue(RegexValidator.validEmailAddress("emily.williams@service.co"));
    }

    @Test
    void validEmailAddress7() {
        assertTrue(RegexValidator.validEmailAddress("alex.brown@company.biz"));

    }

    @Test
    void validEmailAddress8() {
        assertTrue(RegexValidator.validEmailAddress("robert.davis@xyzmail.net"));
    }

    @Test
    void validEmailAddress9() {
        assertTrue(RegexValidator.validEmailAddress("linda.martin@abc.com"));
    }

    @Test
    void validEmailAddress10() {
        assertTrue(RegexValidator.validEmailAddress("charles.white@demo.org"));
    }

    @Test
    void validEmailAddress11() {
        assertTrue(RegexValidator.validEmailAddress("jennifer.moore@website.edu"));
    }

    @Test
    void validEmailAddress12() {
        assertTrue(RegexValidator.validEmailAddress("thomas.miller@workplace.co"));
    }

    @Test
    void validEmailAddress13() {
        assertTrue(RegexValidator.validEmailAddress("natalie.clark@company123.net"));
    }

    @Test
    void validEmailAddress14() {
        assertTrue(RegexValidator.validEmailAddress("james.garcia@testingemail.com"));
    }

    @Test
    void validEmailAddress15() {
        assertTrue(RegexValidator.validEmailAddress("lucy.rodriguez@domaininfo.org"));
    }

    @Test
    void validEmailAddress16() {
        assertTrue(RegexValidator.validEmailAddress("brian.hall@samplemail.biz"));
    }

    @Test
    void validEmailAddress17() {
        assertTrue(RegexValidator.validEmailAddress("katherine.young@webservice.co"));
    }

    @Test
    void validEmailAddress18() {
        assertTrue(RegexValidator.validEmailAddress("kevin.adams@demoemail.net"));
    }

    @Test
    void validEmailAddress19() {
        assertTrue(RegexValidator.validEmailAddress("deborah.scott@companyxyz.com"));
    }

    @Test
    void validEmailAddress20() {
        assertTrue(RegexValidator.validEmailAddress("patrick.taylor@mockmail.org"));
    }

    @Test
    void validEmailAddress21() {
        assertTrue(RegexValidator.validEmailAddress("mia.lopez@servicepro.com"));
    }

    @Test
    void validEmailAddress22() {
        assertTrue(RegexValidator.validEmailAddress("daniel.johnson@fakemail.net"));
    }

    @Test
    void invalidEmailAddress1() {
        assertFalse(RegexValidator.validEmailAddress("plainaddress"));
    }

    @Test
    void invalidEmailAddress2() {
        assertFalse(RegexValidator.validEmailAddress("@missingusername.com"));
    }

    @Test
    void invalidEmailAddress3() {
        assertFalse(RegexValidator.validEmailAddress("missingdomain@.com"));
    }

    @Test
    void invalidEmailAddress4() {
        assertFalse(RegexValidator.validEmailAddress("missingat.com"));
    }

    @Test
    void invalidEmailAddress5() {
        assertFalse(RegexValidator.validEmailAddress("user@domain"));
    }

    @Test
    void invalidEmailAddress6() {
        assertFalse(RegexValidator.validEmailAddress("user@domain@domain.com"));
    }

    @Test
    void invalidEmailAddress7() {
        assertFalse(RegexValidator.validEmailAddress("user@.com"));
    }

    @Test
    void invalidEmailAddress8() {
        assertFalse(RegexValidator.validEmailAddress("user@domain..com"));
    }

    @Test
    void invalidEmailAddress9() {
        assertFalse(RegexValidator.validEmailAddress("user@domain!com"));
    }

    @Test
    void invalidEmailAddress10() {
        assertFalse(RegexValidator.validEmailAddress("user@domain#.com"));
    }

    @Test
    void invalidEmailAddress11() {
        assertFalse(RegexValidator.validEmailAddress("user@domain.com."));
    }

    @Test
    void invalidEmailAddress12() {
        assertFalse(RegexValidator.validEmailAddress("us..er@domain.com"));
    }

    @Test
    void invalidEmailAddress13() {
        assertFalse(RegexValidator.validEmailAddress("user@doma_in.com"));
    }

    @Test
    void invalidEmailAddress14() {
        assertFalse(RegexValidator.validEmailAddress("user@domain_com"));
    }

    @Test
    void invalidEmailAddress15() {
        assertFalse(RegexValidator.validEmailAddress("user@domain#com"));
    }

    @Test
    void invalidEmailAddress16() {
        assertFalse(RegexValidator.validEmailAddress("user@domaincom."));
    }

    @Test
    void invalidEmailAddress17() {
        assertFalse(RegexValidator.validEmailAddress("user@domain....com"));
    }

    @Test
    void invalidEmailAddress18() {
        assertFalse(RegexValidator.validEmailAddress("user@com"));
    }

    @Test
    void invalidEmailAddress19() {
        assertFalse(RegexValidator.validEmailAddress("us..er@domain.com"));
    }

    @Test
    void invalidEmailAddress20() {
        assertFalse(RegexValidator.validEmailAddress(".user@domain.com"));
    }

    @Test
    void invalidEmailAddress21() {
        assertFalse(RegexValidator.validEmailAddress("user.@domain.com"));
    }

    @Test
    void invalidEmailAddress22() {
        assertFalse(RegexValidator.validEmailAddress("us!!er@domain.com"));
    }

    @Test
    void invalidPassword1() {
        assertFalse(RegexValidator.validPassword("abc123"));
    }

    @Test
    void invalidPassword2() {
        assertFalse(RegexValidator.validPassword("1234"));
    }

    @Test
    void invalidPassword3() {
        assertFalse(RegexValidator.validPassword("password"));
    }

    @Test
    void invalidPassword4() {
        assertFalse(RegexValidator.validPassword("12345"));
    }

    @Test
    void invalidPassword5() {
        assertFalse(RegexValidator.validPassword("admin"));
    }

    @Test
    void invalidPassword6() {
        assertFalse(RegexValidator.validPassword("qwerty"));
    }

    @Test
    void invalidPassword7() {
        assertFalse(RegexValidator.validPassword("hello"));
    }

    @Test
    void invalidPassword8() {
        assertFalse(RegexValidator.validPassword("123"));
    }

    @Test
    void invalidPassword9() {
        assertFalse(RegexValidator.validPassword("welcome"));
    }

    @Test
    void invalidPassword10() {
        assertFalse(RegexValidator.validPassword("summer"));
    }

    @Test
    void invalidPassword11() {
        assertFalse(RegexValidator.validPassword("1qaz"));
    }

    @Test
    void invalidPassword12() {
        assertFalse(RegexValidator.validPassword("abc"));
    }

    @Test
    void invalidPassword13() {
        assertFalse(RegexValidator.validPassword("00000"));
    }

    @Test
    void invalidPassword14() {
        assertFalse(RegexValidator.validPassword("love"));
    }

    @Test
    void invalidPassword15() {
        assertFalse(RegexValidator.validPassword("test"));
    }

    @Test
    void invalidPassword16() {
        assertFalse(RegexValidator.validPassword("987654"));
    }

    @Test
    void invalidPassword17() {
        assertFalse(RegexValidator.validPassword("monkey"));
    }

    @Test
    void invalidPassword18() {
        assertFalse(RegexValidator.validPassword("apple"));
    }

    @Test
    void invalidPassword19() {
        assertFalse(RegexValidator.validPassword("secret"));
    }

    @Test
    void invalidPassword20() {
        assertFalse(RegexValidator.validPassword("sunshine"));
    }

    @Test
    void invalidPassword21() {
        assertFalse(RegexValidator.validPassword("password1"));
    }

    @Test
    void invalidPassword22() {
        assertFalse(RegexValidator.validPassword("123qwe"));
    }

    @Test
    void invalidPassword23() {
        assertFalse(RegexValidator.validPassword("qwertyui"));
    }

    @Test
    void invalidPassword24() {
        assertFalse(RegexValidator.validPassword("123asd"));
    }

}