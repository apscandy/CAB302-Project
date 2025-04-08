package com.cab302.cab302project.model.card;

import com.cab302.cab302project.model.deck.Deck;
import java.util.List;

public interface ICardDAO {
    public void addCard(Card card);
    public void updateCard(Card card);
    public void softDeleteCard(Card card);
    public List<Card> getCardsForDeck(Deck deck);

    // Optional: used when deleting a deck
    public void softDeleteCardsByDeck(int deckId);
}
