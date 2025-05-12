package com.cab302.cab302project;

import com.cab302.cab302project.error.state.UserAlreadyLoggedInException;
import com.cab302.cab302project.error.state.UserIsNullException;
import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.user.User;


/**
 * Utility class holds static methods for application state management
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public final class ApplicationState {
    private static User currentUser = null;
    private static boolean userLoggedIn = false;
    private static Deck currentDeck = null;
    private static TestModes currentMode = null;

    /**
     * A user can not be logged in if another user is already logged in
     * otherwise it will throw and unchecked exception and
     * the user will not be logged in.
     * This is an exception based singleton patten
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     * @param user The user to be logged in
     * @throws UserAlreadyLoggedInException Thrown if a user is already logged in
     * @throws UserIsNullException Thrown if the user is null or the id is null
     */
    public static void login(User user){
        if (userLoggedIn && user != null) {
            throw new UserAlreadyLoggedInException("User already logged in");
        }
        try {
            assert user != null;
        }catch (Exception e){
            throw new UserIsNullException(e.getMessage(), e.getCause());
        }
        if (user.getId() == 0) {
            throw new UserIsNullException("User id is null, user may not have been fetched from DB");
        }
        currentUser = user;
        userLoggedIn = true;
    }

    /**
     * Simple function just sets currentUser to null and userLoggedIn to false
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public static void logout() {
        userLoggedIn = false;
        currentUser = null;
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     * @return the current logged-in user or null if there is no user
     * @throws UserIsNullException Thrown if there is no logged-in user to return
     */
    public static User getCurrentUser(){
        if (currentUser == null && !userLoggedIn) {
            throw new UserIsNullException("currentUser is null");
        }
        return currentUser;
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     * @return True if the user is logged in
     */
    public static boolean isUserLoggedIn(){
        return userLoggedIn;
    }

    public static Deck getDeck(){
        return currentDeck;
    }

    public static void setDeck(Deck deck){
        currentDeck = deck;
    }

    public static void clearDeck(){
        currentDeck = null;
    }

    public static void setCurrentModeSequential(){
        currentMode = TestModes.SEQUENTIAL;
    }

    public static void setCurrentModeRandom(){
        currentMode = TestModes.RANDOM;
    }

    public static void setCurrentModeSmart(){
        currentMode = TestModes.SMART;
    }

    public static TestModes getCurrentMode(){
        return currentMode;
    }

    public static void clearCurrentMode(){
        currentMode = null;
    }
}
