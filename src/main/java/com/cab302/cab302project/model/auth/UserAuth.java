package com.cab302.cab302project.model.auth;

import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.user.User;

public class UserAuth {
    public static boolean isAuth = false;
    private static User authUser = null;

    public static boolean auth (String email, String password, SqliteUserDAO userDAO) {
        User user = userDAO.getUser(email);
        if (user == null) {
            return false;
        }
        if (user.getPassword().equals(password)) {
            authUser = user;
            isAuth = true;
            return true;
        }
        return false;
    }

    public static void logoff() {
        isAuth = false;
        authUser = null;
    }

    public static User getAuthUser() {
        return authUser;
    }

}

