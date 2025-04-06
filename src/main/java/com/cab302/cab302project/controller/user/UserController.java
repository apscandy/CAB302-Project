package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.user.User;

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

    public static boolean resetPassword (String email, String newPassword, SqliteUserDAO userDAO) {
        if (email.trim().isEmpty() || newPassword.trim().isEmpty()) {
            return false;
        }
        User checkUser = userDAO.getUser(email);
        if (checkUser != null) {
            checkUser.setPassword(newPassword);
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
