package com.cab302.cab302project.model.session;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.TestModes;
import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.user.User;

import java.time.LocalDateTime;

public final class Session {

    private int id;
    private final Deck deck;
    private final User user;
    private final LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private boolean sessionFinished;
    private TestModes testMode;

    public Session(Deck deck, User user) {
        this.deck = deck;
        this.user = user;
        this.startDateTime = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public int getDeckId() {
        return deck.getId();
    }

    public int getUserId() {
        return user.getId();
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    void setEndDateTime() {
        this.endDateTime = LocalDateTime.now();
    }

    void setSessionFinished() {
        this.sessionFinished = true;
    }

    public boolean getSessionFinished() {
        return sessionFinished;
    }

    public TestModes getTestMode() {
        return testMode;
    }

    void setTestMode() {
        this.testMode = ApplicationState.getCurrentMode();
    }

}
