package com.cab302.cab302project.model.userSecQuestions;
import com.cab302.cab302project.model.user.User;
import java.util.List;

public interface IUserSecurityQuestionDAO {
    void createQuestion(UserSecurityQuestion question);
    UserSecurityQuestion getQuestions(User user);
    void updateQuestions(UserSecurityQuestion updatedQuestion);
}
