package com.cab302.cab302project.model.userSecQuestions;

import com.cab302.cab302project.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserSecurityQuestionTest {

    private UserSecurityQuestion userSecurityQuestion;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("andy", "clarke", "andy@andy.com", "password");
        userSecurityQuestion = new UserSecurityQuestion(user);
        userSecurityQuestion.setQuestionOne("What is your first name?");
        userSecurityQuestion.setQuestionTwo("What is your last name?");
        userSecurityQuestion.setQuestionThree("What is your email?");
        userSecurityQuestion.setAnswerOne("andy");
        userSecurityQuestion.setAnswerTwo("clarke");
        userSecurityQuestion.setAnswerThree("andy@andy.com");
    }

    @Test
    void getQuestionOne() {
        assertEquals("What is your first name?", userSecurityQuestion.getQuestionOne());
    }


    @Test
    void getQuestionTwo() {
        assertEquals("What is your last name?", userSecurityQuestion.getQuestionTwo());
    }

    @Test
    void getQuestionThree() {
        assertEquals("What is your email?", userSecurityQuestion.getQuestionThree());
    }

    @Test
    void getAnswerOne() {
        assertEquals("andy", userSecurityQuestion.getAnswerOne());
    }

    @Test
    void getAnswerTwo() {
        assertEquals("clarke", userSecurityQuestion.getAnswerTwo());
    }

    @Test
    void getAnswerThree() {
        assertEquals("andy@andy.com", userSecurityQuestion.getAnswerThree());
    }

    @Test
    void setQuestionOne() {
        userSecurityQuestion.setQuestionOne("test 1");
        assertEquals("test 1", userSecurityQuestion.getQuestionOne());
    }

    @Test
    void setQuestionTwo() {
        userSecurityQuestion.setQuestionTwo("test 2");
        assertEquals("test 2", userSecurityQuestion.getQuestionTwo());
    }

    @Test
    void setQuestionThree() {
        userSecurityQuestion.setQuestionThree("test 3");
    }

    @Test
    void setAnswerOne() {
        userSecurityQuestion.setAnswerOne("test 1 a");
        assertEquals("test 1 a", userSecurityQuestion.getAnswerOne());
    }

    @Test
    void setAnswerTwo() {
        userSecurityQuestion.setAnswerTwo("test 2 a");
        assertEquals("test 2 a", userSecurityQuestion.getAnswerTwo());
    }

    @Test
    void setAnswerThree() {
        userSecurityQuestion.setAnswerThree("test 3 a");
        assertEquals("test 3 a", userSecurityQuestion.getAnswerThree());
    }
}