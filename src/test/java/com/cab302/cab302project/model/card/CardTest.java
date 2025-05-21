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
    void testGetQuestion() {
        assertEquals("When was Rome founded?", card.getQuestion());
    }

    @Test
    void testGetAnswer() {
        assertEquals("753 BC", card.getAnswer());
    }

    @Test
    void testGetTags() {
        assertEquals("history,rome", card.getTags());
    }

    @Test
    void tesSetQuestion(){
        card.setQuestion("What year did WW2 end?");
        assertEquals("What year did WW2 end?", card.getQuestion());
    }

    @Test
    void tesSetAnswer(){
        card.setAnswer("1945");
        assertEquals("1945", card.getAnswer());
    }

    @Test
    void tesSetTags(){
        card.setTags("history,ww2");
        assertEquals("history,ww2", card.getTags());
    }

    @Test
    void testGetIsDeleted() {
        assertFalse(card.isDeleted());
    }

    @Test
    void testSetIsDeleted() {
        card.setDeleted(true);
        assertTrue(card.isDeleted());
    }

    @Test
    void testSetId(){
        // package‐private setId is visible here because tests share the same package
        card.setId(42);
        assertEquals(42, card.getId());
    }

    @Test
    void testToString(){
        assertEquals("When was Rome founded?", card.toString());
    }
}
