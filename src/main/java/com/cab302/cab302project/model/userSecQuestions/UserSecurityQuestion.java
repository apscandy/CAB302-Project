package com.cab302.cab302project.model.userSecQuestions;

import com.cab302.cab302project.model.user.User;

/**
 * Model class representing a user's security questions and answers.
 * <p>
 * Encapsulates three security questions and their corresponding answers
 * for a specific user, used during password reset authentication.
 * Each instance is tied to a User object and provides getter and setter
 * methods for managing the security question data. The class maintains
 * the association between questions and answers while ensuring data
 * integrity through immutable user reference. Used by the authentication
 * system to validate user identity during password recovery processes.
 * </p>
 * @author Hoang Dat Bui (n11659831, hoangdat.bui@connect.qut.edu.au), Andrew Clarke (n11270179, a40.clarke@connect.qut.edu.au)
 **/

public final class UserSecurityQuestion {
    private final User user;
    private String questionOne;
    private String questionTwo;
    private String questionThree;
    private String answerOne;
    private String answerTwo;
    private String answerThree;

    public UserSecurityQuestion(User user) {
        this.user = user;
    }

    int getUserId() {
        return this.user.getId();
    }

    public String getQuestionOne() {
        return this.questionOne;
    }

    public String getQuestionTwo() {
        return this.questionTwo;
    }

    public String getQuestionThree() {
        return this.questionThree;
    }

    public String getAnswerOne() {
        return this.answerOne;
    }

    public String getAnswerTwo() {
        return this.answerTwo;
    }

    public String getAnswerThree() {
        return this.answerThree;
    }

    public void setQuestionOne(String questionOne) {
        this.questionOne = questionOne;
    }

    public void setQuestionTwo(String questionTwo) {
        this.questionTwo = questionTwo;
    }

    public void setQuestionThree(String questionThree) {
        this.questionThree = questionThree;
    }

    public void setAnswerOne(String answerOne) {
        this.answerOne = answerOne;
    }

    public void setAnswerTwo(String answerTwo) {
        this.answerTwo = answerTwo;
    }

    public void setAnswerThree(String answerThree) {
        this.answerThree = answerThree;
    }
}
