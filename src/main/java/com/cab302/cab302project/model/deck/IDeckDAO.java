package com.cab302.cab302project.model.deck;

import com.cab302.cab302project.model.user.User;

import java.util.List;

/**
 * Interface defining operations for managing decks (collections of cards) in the application.
 * This interface abstracts data access logic for decks, including creation, updating,
 * deletion, restoration, and retrieval operations. It supports both hard-deletion and
 * soft-deletion mechanisms to handle deck lifecycle management.
 *
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public interface IDeckDAO {

    /**
     * Creates a new deck in the database.
     * This method persists the provided deck object, assigning it a unique ID and
     * associating it with the specified user.
     *
     * @param deck The Deck object to be created. Must not have an existing ID.
     */
    void createDeck(Deck deck);

    /**
     * Updates an existing deck in the database.
     * This method modifies the provided deck's properties (e.g., name, description,
     * cards) and persists the changes.
     *
     * @param deck The Deck object with updated data. Must have a valid ID assigned.
     */
    void updateDeck(Deck deck);

    /**
     * Permanently deletes a deck from the database.
     * This operation removes all records associated with the deck and cannot be
     * reversed unless using the soft-delete mechanism.
     *
     * @param deck The Deck object to be permanently deleted. Must have a valid ID.
     */
    void deleteDeck(Deck deck);

    /**
     * Soft-deletes a deck (marks it as deleted without removing data).
     * This operation sets a flag on the deck indicating it is no longer active,
     * but retains all associated data for potential restoration.
     *
     * @param deck The Deck object to be soft-deleted. Must have a valid ID.
     */
    void softDeleteDeck(Deck deck);

    /**
     * Restores a previously soft-deleted deck.
     * This operation reactivates the deck by removing its deleted status and
     * restoring it to an active state in the database.
     *
     * @param deck The Deck object to be restored. Must have been marked as soft-deleted.
     */
    void restoreDeck(Deck deck);

    /**
     * Retrieves all decks owned by a specific user.
     * This method returns a list of all decks associated with the provided user,
     * including both active and soft-deleted decks if not filtered.
     *
     * @param user The User object whose decks are to be retrieved.
     * @return A list of Deck objects owned by the specified user.
     */
    List<Deck> getDecks(User user);

    /**
     * Retrieves all soft-deleted decks owned by a specific user.
     * This method returns a list of decks that have been marked as deleted but
     * are still stored in the database (i.e., not permanently removed).
     *
     * @param user The User object whose soft-deleted decks are to be retrieved.
     * @return A list of Deck objects representing soft-deleted decks owned by the user.
     */
    List<Deck> getSoftDeletedDecks(User user);

    /**
     * Retrieves a single deck by its unique identifier.
     * This method fetches the deck from the database using the provided ID.
     *
     * @param id The unique integer ID of the deck to retrieve.
     * @return The Deck object with the matching ID, or null if not found.
     */
    Deck getDeck(int id);

    /**
     * Sets the bookmark status for a specific deck.
     * This method updates whether the provided deck is marked as bookmarked by its owner.
     *
     * @param deck        The Deck object whose bookmark status is to be updated.
     * @param bookmarked  A boolean indicating the new bookmark state (true = bookmarked, false = unbookmarked).
     */
    void setBookmarked(Deck deck, boolean bookmarked);

    /**
     * Retrieves all decks bookmarked by a specific user.
     * This method returns a list of decks that are marked as bookmarked by the provided user.
     *
     * @param user The User object whose bookmarked decks are to be retrieved.
     * @return A list of Deck objects bookmarked by the specified user.
     */
    List<Deck> getBookmarkedDecks(User user);
}
