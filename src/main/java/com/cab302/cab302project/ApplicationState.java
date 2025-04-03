package com.cab302.cab302project;

import com.cab302.cab302project.model.user.User;

public class ApplicationState {
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
        return currentUser;
    }

    public static boolean isUserLoggedIn() {
        return userLoggedIn;
    }
}
