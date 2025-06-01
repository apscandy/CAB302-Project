package com.cab302.cab302project.model.session;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    private User user;
    private Deck deck;
    private Session session;

    @BeforeEach
    void setUp() {
        user =  new User("test", "Test", "Test@testing.com", "password-very-secure");
        deck = new Deck("Chem 101", "flash cards for chem 101 exam", user);
        session = new Session(deck, user);
        session.setId(12);
    }

    @Test
    void getId() {
        assertEquals(12, session.getId());
    }

    @Test
    void setId() {
        session.setId(14);
        assertEquals(14, session.getId());
    }

    @Test
    void getDeckId() {
        assertEquals(deck.getId(), session.getDeckId());
    }

    @Test
    void getUserId() {
        assertEquals(user.getId(), session.getUserId());
    }


    @Test
    void setEndDateTime() {
        session.setEndDateTime();
        assertNotNull(session.getEndDateTime());
    }

    @Test
    void setSessionFinished() {
        session.setSessionFinished();
        assertNotNull(session.getSessionFinished());
    }

    @Test
    void getSessionFinished() {
        session.setSessionFinished();
        assertEquals(true, session.getSessionFinished());
    }
}