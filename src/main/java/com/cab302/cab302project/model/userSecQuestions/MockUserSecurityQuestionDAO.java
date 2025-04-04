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
    public UserSecurityQuestion getQuestions(int userId) {

        UserSecurityQuestion User = null;

        for (UserSecurityQuestion questions : questionList) {
            if (questions.getUserId() == userId) {

                User = questions;

                return User;
            }
        }
        return User;
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
