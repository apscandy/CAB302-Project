package com.cab302.cab302project.model.userSecQuestions;

import com.cab302.cab302project.model.user.User;

public class UserSecurityQuestion {
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

    public String getQuestionOne() {
        return questionOne;
    }

    public int getUserId() {
        return user.getId();
    }

    public String getQuestionTwo() {
        return questionTwo;
    }

    public String getQuestionThree() {
        return questionThree;
    }

    public String getAnswerOne() {
        return answerOne;
    }

    public String getAnswerTwo() {
        return answerTwo;
    }

    public String getAnswerThree() {
        return answerThree;
    }

    public String setQuestionOne(String questionOne) {
        return this.questionOne = questionOne;
    }

    public String setQuestionTwo(String questionTwo) {
        return this.questionTwo = questionTwo;
    }

    public String setQuestionThree(String questionThree) {
        return this.questionThree = questionThree;
    }

    public String setAnswerOne(String answerOne) {
        return this.answerOne = answerOne;
    }

    public String setAnswerTwo(String answerTwo) {
        return this.answerTwo = answerTwo;
    }

    public String setAnswerThree(String answerThree) {
        return this.answerThree = answerThree;
    }
}
