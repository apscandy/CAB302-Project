package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.user.User;
import com.cab302.cab302project.model.userSecQuestions.SqliteUserSecurityQuestionDAO;
import com.cab302.cab302project.model.userSecQuestions.UserSecurityQuestion;

public class UserController {
    public static boolean authenticate (String email, String password, SqliteUserDAO userDAO) {
        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            return false;
        }
        User user = userDAO.getUser(email);
        if (user == null) {
            return false;
        }
        if (user.getPassword().equals(password)) {
            ApplicationState.login(user);
            return true;
        }
        return false;
    }

    public static boolean emailCheck (String email, SqliteUserDAO userDAO) {
        if (email.trim().isEmpty()) {
            return false;
        }
        User user = userDAO.getUser(email);
        if (user != null) {
            return true;
        }
        return false;
    }

    public static boolean register (User user, SqliteUserDAO userDAO) {
        if (user.getEmail().trim().isEmpty() || user.getPassword().trim().isEmpty() ||
                user.getFirstName().trim().isEmpty() || user.getLastName().trim().isEmpty() ||
                    user == null) {
            return false;
        }
        if (userDAO.getUser(user.getEmail()) == null) {
            userDAO.addUser(user);
            return true;
        }
        return false;
    }

    public boolean resetPassword (String email, String newPassword, SqliteUserDAO userDAO) {
        if (email.trim().isEmpty() || newPassword.trim().isEmpty()) {
            return false;
        }
        User checkUser = userDAO.getUser(email);
        if (checkUser != null) {
            checkUser.setPassword(newPassword);
            userDAO.updateUser(checkUser);
            return true;
        }
        return false;
    }

    public static boolean checkSecurityQuestion (String email, String answerOne, String answerTwo, String answerThree,
                                 SqliteUserSecurityQuestionDAO questionDAO, SqliteUserDAO userDAO) {
        if (answerOne.trim().isEmpty() || answerTwo.trim().isEmpty() || answerThree.trim().isEmpty()
        || email.trim().isEmpty()) {
            return false;
        }
        User user = userDAO.getUser(email);
        if (user == null) {
            return false;
        }
        UserSecurityQuestion questions = questionDAO.getQuestions(user);
        if (questions == null) {
            return false;
        }
        boolean validAnswers = (
                questions.getAnswerOne().equalsIgnoreCase(answerOne.trim()) &&
                questions.getAnswerTwo().equalsIgnoreCase(answerTwo.trim()) &&
                questions.getAnswerThree().equalsIgnoreCase(answerThree.trim())
        );
        if (validAnswers) {
            return true;
        }
        return false;
    }

    public static void logout() {
        ApplicationState.logout();
    }

    public static User getUserProfile (String email, SqliteUserDAO userDAO) {
        return userDAO.getUser(email);
    }
}
