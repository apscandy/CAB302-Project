package com.cab302.cab302project.model.session;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.TestModes;
import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.user.User;

import java.time.LocalDateTime;

/**
 * Represents a session where a user interacts with a deck.
 * A session includes information about the associated deck, user,
 * start and end times, whether it's finished, and the test mode used.
 */
public final class Session {

    /**
     * The unique identifier for this session.
     */
    private int id;

    /**
     * The deck associated with this session.
     */
    private final Deck deck;

    /**
     * The user who initiated this session.
     */
    private final User user;

    /**
     * The date and time when the session started.
     */
    private final LocalDateTime startDateTime;

    /**
     * The date and time when the session ended, if applicable.
     */
    private LocalDateTime endDateTime;

    /**
     * Indicates whether this session has been completed.
     */
    private boolean sessionFinished;

    /**
     * The test mode used during this session (e.g., normal, timer-based).
     */
    private TestModes testMode;

    /**
     * Constructs a new Session with the provided deck and user.
     * The start time is initialized to the current moment.
     *
     * @param deck  The deck associated with this session.
     * @param user  The user who started the session.
     */
    public Session(Deck deck, User user) {
        this.deck = deck;
        this.user = user;
        this.startDateTime = LocalDateTime.now();
    }

    /**
     * Returns the unique identifier for this session.
     *
     * @return The session ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this session.
     *
     * @param id The session ID to set.
     */
    void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the ID of the deck associated with this session.
     *
     * @return The deck's ID.
     */
    public int getDeckId() {
        return deck.getId();
    }

    /**
     * Returns the ID of the user who initiated this session.
     *
     * @return The user's ID.
     */
    public int getUserId() {
        return user.getId();
    }

    /**
     * Returns the date and time when the session started.
     *
     * @return The start datetime.
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * Returns the date and time when the session ended, if applicable.
     *
     * @return The end datetime (or null if not set).
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Sets the end time of this session to the current moment.
     */
    void setEndDateTime() {
        this.endDateTime = LocalDateTime.now();
    }

    /**
     * Marks this session as finished.
     */
    void setSessionFinished() {
        this.sessionFinished = true;
    }

    /**
     * Checks if this session has been completed.
     *
     * @return True if the session is finished, false otherwise.
     */
    public boolean getSessionFinished() {
        return sessionFinished;
    }

    /**
     * Returns the test mode used during this session.
     *
     * @return The test mode (e.g., normal, timer-based).
     */
    public TestModes getTestMode() {
        return testMode;
    }

    /**
     * Sets the test mode for this session based on the current ApplicationState.
     */
    void setTestMode() {
        this.testMode = ApplicationState.getCurrentMode();
    }
}

