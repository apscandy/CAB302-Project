package com.cab302.cab302project;

import com.cab302.cab302project.model.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApplicationState {
    private static final Logger logger = LogManager.getLogger(ApplicationState.class);
    private static User currentUser = null;
    private static boolean userLoggedIn = false;

    public static void login(User user) {
        currentUser = user;
        userLoggedIn = true;
    }

    public static void logout() {
        userLoggedIn = false;
        currentUser = null;
    }

    public static User getCurrentUser() {
        if (currentUser == null) {
            logger.warn("currentUser is null");
        }
        return currentUser;
    }

    public static boolean isUserLoggedIn() {
        if (!userLoggedIn) {
            logger.warn("userLoggedIn is false");
        }
        return userLoggedIn;
    }
}
