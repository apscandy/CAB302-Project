package com.cab302.cab302project.model.userSecQuestions;

import com.cab302.cab302project.model.user.User;

/**
 * @author Hoang Dat Bui, Andrew Clarke
 */

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
