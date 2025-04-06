package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.user.User;
import com.cab302.cab302project.model.userSecQuestions.SqliteUserSecurityQuestionDAO;
import com.cab302.cab302project.model.userSecQuestions.UserSecurityQuestion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);
    public static boolean authenticate (String email, String password, SqliteUserDAO userDAO) {
        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            logger.warn("Authentication failed: email or password is empty");
            return false;
        }
        User user = userDAO.getUser(email);
        if (user == null) {
            logger.warn("Authentication failed: user not found");
            return false;
        }
        if (user.getPassword().equals(password)) {
            logger.info("Authentication successful");
            ApplicationState.login(user);
            return true;
        } else {
            logger.warn("Authentication failed: incorrect password for email");
        }
        return false;
    }

    public static boolean emailCheck (String email, SqliteUserDAO userDAO) {
        if (email.trim().isEmpty()) {
            logger.warn("Email check failed: empty email provided");
            return false;
        }
        User user = userDAO.getUser(email);
        if (user != null) {
            logger.info("Email check successful");
            return true;
        } else {
            logger.warn("Email check failed");
        }
        return false;
    }

    public static boolean register (User user, SqliteUserDAO userDAO) {
        if (user == null) {
            logger.warn("Registration failed: user is null");
        }
        if (user.getEmail().trim().isEmpty() || user.getPassword().trim().isEmpty() ||
                user.getFirstName().trim().isEmpty() || user.getLastName().trim().isEmpty()) {
            logger.warn("Registration failed: missing or invalid user data");
            return false;
        }
        if (userDAO.getUser(user.getEmail()) == null) {
            userDAO.addUser(user);
            logger.info("User registration successful");
            return true;
        } else {
            logger.warn("Registration failed: user already exists");
        }
        return false;
    }

    public boolean resetPassword (String email, String newPassword, SqliteUserDAO userDAO) {
        if (email.trim().isEmpty() || newPassword.trim().isEmpty()) {
            logger.warn("Password reset failed: email or new password is empty");
            return false;
        }
        User checkUser = userDAO.getUser(email);
        if (checkUser != null) {
            checkUser.setPassword(newPassword);
            userDAO.updateUser(checkUser);
            logger.info("Password reset successful");
            return true;
        } else {
            logger.warn("Password reset failed: user not found");
        }
        return false;
    }

    public static boolean checkSecurityQuestion (String email, String answerOne, String answerTwo, String answerThree,
                                 SqliteUserSecurityQuestionDAO questionDAO, SqliteUserDAO userDAO) {
        if (answerOne.trim().isEmpty() || answerTwo.trim().isEmpty() || answerThree.trim().isEmpty()
        || email.trim().isEmpty()) {
            logger.warn("Security question validation failed: missing input");
            return false;
        }
        User user = userDAO.getUser(email);
        if (user == null) {
            logger.warn("Security question validation failed: user not found");
            return false;
        }
        UserSecurityQuestion questions = questionDAO.getQuestions(user);
        if (questions == null) {
            logger.warn("Security question validation failed: security questions for user not found");
            return false;
        }
        return  questions.getAnswerOne().equalsIgnoreCase(answerOne.trim()) &&
                questions.getAnswerTwo().equalsIgnoreCase(answerTwo.trim()) &&
                questions.getAnswerThree().equalsIgnoreCase(answerThree.trim());
    }

    public static void logout() {
        logger.info("Logging out current user.");
        ApplicationState.logout();
    }
}
