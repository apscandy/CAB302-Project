package com.cab302.cab302project;

import com.cab302.cab302project.error.state.UserAlreadyLoggedInException;
import com.cab302.cab302project.error.state.UserIsNullException;
import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.user.User;


/**
 * Utility class for managing application-wide state, including user authentication,
 * current deck selection, and test mode configurations.
 *
 * <p>This class follows an exception-based singleton pattern to ensure only one user
 * can be logged in at a time. It provides methods to manage the login/logout process,
 * track the currently active deck, and configure test modes for learning sessions.</p>
 *
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public final class ApplicationState {

    /**
     * The currently logged-in user.
     * This field is initialized to null by default and should only be set through the {@link #login(User)} method.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    private static User currentUser = null;

    /**
     * Indicates whether a user is currently logged in.
     * This flag ensures that multiple users cannot log in simultaneously.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    private static boolean userLoggedIn = false;

    /**
     * The deck currently being used for learning or testing.
     * This field is initialized to null and should only be set through the {@link #setDeck(Deck)} method.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    private static Deck currentDeck = null;

    /**
     * The test mode currently active, which dictates how flashcards are presented during review sessions.
     * Supported modes include SEQUENTIAL, RANDOM, and SMART.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
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


    /**
     * Retrieves the currently active deck.
     *
     * <p>This method returns the deck that is currently being used for learning or testing.
     * If no deck has been set, it will return null.</p>
     *
     * @return The current deck, or `null` if none is selected.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public static Deck getDeck(){
        return currentDeck;
    }

    /**
     * Sets the currently active deck for learning/testing.
     *
     * <p>This method allows replacing the existing deck with a new one. It will overwrite
     * any previously set deck value.</p>
     *
     * @param deck The new deck to be used.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public static void setDeck(Deck deck){
        currentDeck = deck;
    }

    /**
     * Clears the currently active deck.
     *
     * <p>This method sets the `currentDeck` field to null, effectively deselecting
     * any previously chosen deck.</p>
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public static void clearDeck(){
        currentDeck = null;
    }

    /**
     * Sets the test mode to sequential card review (card 1 → card 2 → ...).
     *
     * <p>This method updates the `currentMode` field to indicate that cards should
     * be reviewed in their original order.</p>
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public static void setCurrentModeSequential(){
        currentMode = TestModes.SEQUENTIAL;
    }

    /**
     * Sets the test mode to random card review (cards are shuffled for each session).
     *
     * <p>This method updates the `currentMode` field to indicate that cards should
     * be reviewed in a randomized order.</p>
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public static void setCurrentModeRandom(){
        currentMode = TestModes.RANDOM;
    }

    /**
     * Sets the test mode to smart card review (cards are prioritized based on user performance).
     *
     * <p>This method updates the `currentMode` field to indicate that cards should
     * be reviewed using an adaptive algorithm for optimal learning.</p>
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public static void setCurrentModeSmart(){
        currentMode = TestModes.SMART;
    }


    /**
     * Retrieves the currently active test mode.
     *
     * <p>This method returns the mode that dictates how flashcards are presented
     * during review sessions. Possible values include SEQUENTIAL, RANDOM, and SMART.</p>
     *
     * @return The current test mode, or `null` if no mode has been set.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public static TestModes getCurrentMode(){
        return currentMode;
    }

    /**
     * Clears the currently active test mode.
     *
     * <p>This method sets the `currentMode` field to null, effectively disabling
     * any previously configured test mode.</p>
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public static void clearCurrentMode(){
        currentMode = null;
    }
}
