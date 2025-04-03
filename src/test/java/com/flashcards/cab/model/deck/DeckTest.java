package com.flashcards.cab.model.deck;

import com.flashcards.cab.model.card.Card;
import com.flashcards.cab.model.user.User;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    private Deck deck;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("Andrew", "Clarke", "legend@qut.edu.au", "VeryStrongPassword");
        user.setId(68);
        deck = new Deck("quantum computing", "I feel very stupid", user, new ArrayList<>());
    }

    @Test
    void getId() {
        assertEquals(0, deck.getId());
    }

    @Test
    void setId() {
        deck.setId(1);
        assertEquals(1, deck.getId());
    }

    @Test
    void getUserId() {
        assertEquals(68, deck.getUserId());
    }

    @Test
    void getName() {
        assertEquals("quantum computing", deck.getName());
    }

    @Test
    void setName() {
        deck.setName("quantum computing 2 electric boogaloo");
        assertEquals("quantum computing 2 electric boogaloo", deck.getName());
    }

    @Test
    void getDescription() {
        assertEquals("I feel very stupid", deck.getDescription());
    }

    @Test
    void setDescription() {
        deck.setDescription("The quick brown fox jumps over the lazy dog");
        assertEquals("The quick brown fox jumps over the lazy dog", deck.getDescription());
    }


    @Test
    void getCards() {
        assertEquals(0, deck.getCards().size());
    }

    @Test
    void setCards() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card());
        cards.add(new Card());
        cards.add(new Card());
        deck.setCards(cards);
        assertEquals(3, deck.getCards().size());

    }
}