package com.cab302.cab302project.model.userSecQuestions;

import java.util.List;
import java.util.ArrayList;

public class MockUserSecurityQuestionDAO implements IUserSecurityQuestionDAO {

    private int autoincrement = 1;
    private final List<UserSecurityQuestion> questionList = new ArrayList<>();

    public MockUserSecurityQuestionDAO() {

    }

    @Override
    public void createQuestion(UserSecurityQuestion question) {
        questionList.add(question);
    }

    @Override
    public List<UserSecurityQuestion> getQuestions(int userId) {
        List<UserSecurityQuestion> userQuestions = new ArrayList<>();
        for (UserSecurityQuestion questions : questionList) {
            if (questions.getUserId() == userId) {
                userQuestions.add(questions);
            }
        }
        return userQuestions;
    }

    public void updateQuestions(UserSecurityQuestion updatedQuestions) {
        for (int i = 0; i < questionList.size(); i++) {
            if (questionList.get(i).getUserId() == updatedQuestions.getUserId()) {
                questionList.set(i, updatedQuestions);
                return;
            }
        }
    }
}
