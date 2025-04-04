package com.cab302.cab302project.model.userSecQuestions;
import java.util.List;

public interface IUserSecurityQuestionDAO {
    void createQuestion(UserSecurityQuestion question);
    UserSecurityQuestion getQuestions(int userId);
    void updateQuestions(UserSecurityQuestion updatedQuestion);
}
