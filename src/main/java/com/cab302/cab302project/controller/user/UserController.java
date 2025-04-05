package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.user.User;
import com.cab302.cab302project.ApplicationState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class UserController {
    public static void authenticate (String email, String password, SqliteUserDAO userDAO) {
        User user = userDAO.getUser(email);
        if (user == null) {
            return;
        }
        if (user.getPassword().equals(password)) {
            ApplicationState.login(user);
        }
    }
}
