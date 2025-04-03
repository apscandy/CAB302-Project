package com.cab302.cab302project.model.userSecQuestions;

import java.util.List;
import java.util.ArrayList;
import com.cab302.cab302project.model.userSecQuestions.UserSecurityQuestion;

public class MockUserSecurityQuestionDAO {

    private int autoincrement = 1;
    private final List<UserSecurityQuestion> questionList = new ArrayList<>();

    public MockUserSecurityQuestionDAO() {
    }

    public void createQuestions(UserSecurityQuestion questions) {
        questions.setId(autoincrement++);
        questionList.add(questions);
    }

    public UserSecurityQuestion getQuestions(int userId) {
        for (UserSecurityQuestion questions : questionList) {
            if (questions.getUserId() == userId) {
                return questions;
            }
        }
        return null;
    }

    public void updateQuestions(UserSecurityQuestion updatedQuestions) {
        for (int i = 0; i < questionList.size(); i++) {
            if (questionList.get(i).getUserId() == updatedQuestions.getUserId) {
                questionList.set(i, updatedQuestions);
                return;
            }
        }
    }
}
