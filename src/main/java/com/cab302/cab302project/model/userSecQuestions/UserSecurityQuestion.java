package com.cab302.cab302project.model.userSecQuestions;

import com.cab302.cab302project.model.user.User;

/**
 * Represents the security questions and answers associated with a user.
 * This class encapsulates three security questions and their corresponding answers,
 * which are used for password recovery or account verification purposes.
 *
 * <p>This class is immutable once constructed, as it holds a final reference to the user.</p>
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

    /**
     * Constructs a new {@code UserSecurityQuestion} instance associated with the provided user.
     *
     * @param user The user object to associate with this security question set.
     *             This reference is stored as final and cannot be modified after construction.
     */
    public UserSecurityQuestion(User user) {
        this.user = user;
    }

    /**
     * Returns the unique identifier of the user associated with these security questions.
     *
     * @return The ID of the user.
     */
    int getUserId() {
        return this.user.getId();
    }

    /**
     * Returns the first security question associated with the user.
     *
     * @return The first security question (e.g., "What is your mother's maiden name?").
     */
    public String getQuestionOne() {
        return this.questionOne;
    }

    /**
     * Returns the second security question associated with the user.
     *
     * @return The second security question.
     */
    public String getQuestionTwo() {
        return this.questionTwo;
    }

    /**
     * Returns the third security question associated with the user.
     *
     * @return The third security question.
     */
    public String getQuestionThree() {
        return this.questionThree;
    }

    /**
     * Returns the answer to the first security question.
     *
     * @return The answer for the first question.
     */
    public String getAnswerOne() {
        return this.answerOne;
    }

    /**
     * Returns the answer to the second security question.
     *
     * @return The answer for the second question.
     */
    public String getAnswerTwo() {
        return this.answerTwo;
    }

    /**
     * Returns the answer to the third security question.
     *
     * @return The answer for the third question.
     */
    public String getAnswerThree() {
        return this.answerThree;
    }

    /**
     * Sets the first security question for the user.
     *
     * @param questionOne The first security question to set.
     */
    public void setQuestionOne(String questionOne) {
        this.questionOne = questionOne;
    }

    /**
     * Sets the second security question for the user.
     *
     * @param questionTwo The second security question to set.
     */
    public void setQuestionTwo(String questionTwo) {
        this.questionTwo = questionTwo;
    }

    /**
     * Sets the third security question for the user.
     *
     * @param questionThree The third security question to set.
     */
    public void setQuestionThree(String questionThree) {
        this.questionThree = questionThree;
    }

    /**
     * Sets the answer to the first security question.
     *
     * @param answerOne The answer to set for the first question.
     */
    public void setAnswerOne(String answerOne) {
        this.answerOne = answerOne;
    }

    /**
     * Sets the answer to the second security question.
     *
     * @param answerTwo The answer to set for the second question.
     */
    public void setAnswerTwo(String answerTwo) {
        this.answerTwo = answerTwo;
    }

    /**
     * Sets the answer to the third security question.
     *
     * @param answerThree The answer to set for the third question.
     */
    public void setAnswerThree(String answerThree) {
        this.answerThree = answerThree;
    }
}
