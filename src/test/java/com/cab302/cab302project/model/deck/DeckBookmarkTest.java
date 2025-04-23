package com.cab302.cab302project.model.deck;

import com.cab302.cab302project.model.user.User;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DeckBookmarkTest {
    private Deck deck;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("A","B","a@b","pwd");
        deck = new Deck("Title","Desc", user, new ArrayList<>());
    }

    @Test
    void defaultNotBookmarked() {
        assertFalse(deck.isBookmarked(), "New decks should not be bookmarked by default");
    }

    @Test
    void canToggleBookmarkFlag() {
        deck.setBookmarked(true);
        assertTrue(deck.isBookmarked());
        deck.setBookmarked(false);
        assertFalse(deck.isBookmarked());
    }
}
