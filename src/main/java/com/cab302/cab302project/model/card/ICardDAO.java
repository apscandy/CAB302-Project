package com.cab302.cab302project.model.card;

import com.cab302.cab302project.model.deck.Deck;
import java.util.List;

/**
 * Interface for data access operations for Card objects.
 * Provides methods to add, update, and soft delete cards, as well as to retrieve cards for a given deck.
 *
 * @author Monica Borg (n9802045)
 */
public interface ICardDAO {
    /**
     * Adds a card to the database.
     *
     * @param card the card to add
     */
    void addCard(Card card);

    /**
     * Updates an existing card in the database.
     *
     * @param card the card to update
     */
    void updateCard(Card card);

    /**
     * Soft deletes a card (marks it as deleted) in the database.
     *
     * @param card the card to soft delete
     */
    void softDeleteCard(Card card);

    /**
     * Retrieves a list of non-deleted cards for the specified deck.
     *
     * @param deck the deck for which to retrieve cards
     * @return a list of cards in the deck
     */
    List<Card> getCardsForDeck(Deck deck);

    /**
     * Soft deletes all cards associated with the specified deck.
     *
     * @param deck the deck whose cards will be soft deleted
     */
    void softDeleteCardsByDeck(Deck deck);


    /**
     *
     * @param deck Deck to load Cards into
     */
    void getCardAndLoadIntoDeck(Deck deck);
}
