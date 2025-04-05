package com.cab302.cab302project.model.userSecQuestions;

import com.cab302.cab302project.model.user.User;

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
    public UserSecurityQuestion getQuestions(User user) {
        return null;
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
