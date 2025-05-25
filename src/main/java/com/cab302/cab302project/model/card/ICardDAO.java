package com.cab302.cab302project.model.card;

import com.cab302.cab302project.model.deck.Deck;
import java.util.List;

/**
 * Interface for data access operations for Card objects.
 * Provides methods to add, update, and soft delete cards, as well as to retrieve cards for a given deck.
 *
 * @author Monica Borg (n09802045) (monica.borg@connect.qut.edu.au)
 */
public interface ICardDAO {
    /**
     * Adds a card to the database.
     *
     * @param card the card to add
     * @author Monica Borg (n09802045) (monica.borg@connect.qut.edu.au)
     */
    void addCard(Card card);

    /**
     * Updates an existing card in the database.
     *
     * @param card the card to update
     * @author Monica Borg (n09802045) (monica.borg@connect.qut.edu.au)
     */
    void updateCard(Card card);

    /**
     * Delete an existing card in the database.
     *
     * @param card the card to update
     * @author David Bui (n11659831) (hoangdat.bui@connect.qut.edu.au)
     */
    void deleteCard(Card card);

    /**
     * Soft deletes a card (marks it as deleted) in the database.
     *
     * @param card the card to soft delete
     * @author Monica Borg (n09802045) (monica.borg@connect.qut.edu.au)
     */
    void softDeleteCard(Card card);

    /**
     * Restores a car in the database.
     *
     * @param card the card
     * @author David Bui (n11659831) (hoangdat.bui@connect.qut.edu.au)
     */
    void restoreCard(Card card);

    /**
     * Retrieves a list of non-deleted cards for the specified deck.
     *
     * @param deck the deck for which to retrieve cards
     * @return a list of cards in the deck
     * @author Monica Borg (n09802045) (monica.borg@connect.qut.edu.au)
     */
    List<Card> getCardsForDeck(Deck deck);


    /**
     * Restores all cards associated with the specified deck.
     *
     * @param deck the deck whose cards will be soft deleted
     * @author David Bui (n11659831) (hoangdat.bui@connect.qut.edu.au)
     */
    void restoreCardsByDeck(Deck deck);

    /**
     * Gets all soft-deleted cards for a specific deck.
     *
     * @param deck the deck to get soft-deleted cards for
     * @author Monica Borg (n09802045) (monica.borg@connect.qut.edu.au)
     */
    List<Card> getSoftDeletedCardsForDeck(Deck deck);

    /**
     *
     * @param deck Deck to load Cards into
     * @author Lewis Phan (n11781840) (danglinh.phan@connect.qut.edu.au)
     */
    void getCardAndLoadIntoDeck(Deck deck);

    /**
     *
     * @param deck Deck to load Cards into
     * @return the shuffle deck
     * @author Lewis Phan (n11781840) (danglinh.phan@connect.qut.edu.au)
     */
    List<Card> getRandomizedCardsForDeck(Deck deck);
}
