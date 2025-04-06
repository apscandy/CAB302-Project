package com.cab302.cab302project.model.card;

import com.cab302.cab302project.model.deck.Deck;
import java.util.List;

public interface ICardDAO {
    void addCard(Card card);
    void updateCard(Card card);
    void softDeleteCard(Card card);
    List<Card> getCardsForDeck(Deck deck);

    // Optional: used when deleting a deck
    void softDeleteCardsByDeck(int deckId);
}
