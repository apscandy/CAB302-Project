package com.cab302.cab302project.model.card;

import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.user.User;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    private Card card;
    private Deck deck;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("Jane", "Doe", "jane@example.com", "password123");
        deck = new Deck("History", "Ancient civilisations", user, new ArrayList<>());
        card = new Card(deck, "When was Rome founded?", "753 BC", "history,rome");
    }

    @Test
    void testGetDeck() {
        assertSame(deck, card.getDeck());
    }

    @Test
    void testGetQuestionAndAnswerAndTags() {
        assertEquals("When was Rome founded?", card.getQuestion());
        assertEquals("753 BC", card.getAnswer());
        assertEquals("history,rome", card.getTags());
    }

    @Test
    void testSetters() {
        card.setQuestion("What year did WW2 end?");
        card.setAnswer("1945");
        card.setTags("history,ww2");
        assertEquals("What year did WW2 end?", card.getQuestion());
        assertEquals("1945", card.getAnswer());
        assertEquals("history,ww2", card.getTags());
    }

    @Test
    void testDeletedFlag() {
        assertFalse(card.isDeleted());
        card.setDeleted(true);
        assertTrue(card.isDeleted());
    }

    @Test
    void testIdSetterAndToString() {
        // package‐private setId is visible here because tests share the same package
        card.setId(42);
        assertEquals(42, card.getId());

        // explicitly set the question so toString matches
        card.setQuestion("What year did WW2 end?");
        assertEquals("What year did WW2 end?", card.toString());
    }
}
