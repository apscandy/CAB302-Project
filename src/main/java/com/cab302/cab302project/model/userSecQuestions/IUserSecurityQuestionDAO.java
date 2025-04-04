package com.cab302.cab302project.model.userSecQuestions;
import com.cab302.cab302project.model.user.User;
import java.util.List;

public interface IUserSecurityQuestionDAO {
    public void createQuestion(UserSecurityQuestion question);
    UserSecurityQuestion getQuestions(User user);
    public void updateQuestions(UserSecurityQuestion updatedQuestion);
}
