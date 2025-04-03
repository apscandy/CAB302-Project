package com.cab302.cab302project.model.userSecQuestions;

public interface IUserSecurityQuestionDAO {
    void createQuestions(UserSecurityQuestion question);
    UserSecurityQuestion getQuestions(int userId);
    void updateQuestions(UserSecurityQuestion updatedQuestion);
}
