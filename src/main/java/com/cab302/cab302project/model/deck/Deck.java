package com.cab302.cab302project.model.deck;

import com.cab302.cab302project.model.card.Card;
import com.cab302.cab302project.model.user.User;

import java.util.ArrayList;
import java.util.List;



/**
 * Represents a deck of cards owned by a user in the application.
 * A Deck contains a collection of Cards and is associated with one User.
 *
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public final class Deck {
    /**
     * Unique identifier for this deck. Used to distinguish between different decks.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    private int id;

    /**
     * The name of the deck, used for identification and display purposes.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    private String name;

    /**
     * A brief description that explains what the deck is about or its purpose.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    private String description;

    /**
     * Indicates whether this deck is bookmarked by the user. Useful for quick access.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    private Boolean isBookmarked = false;

    /**
     * The user who owns and created this deck.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    private final User user;

    /**
     * The list of cards that make up this deck. Each card has a front and back side.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    private List<Card> cards;

    /**
     * Constructs a new Deck with the given name, description, and owner.
     *
     * @param name        The name of the deck.
     * @param description A brief description about the deck's purpose or content.
     * @param user        The User who created this deck.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public Deck(String name, String description, User user) {
        this.name = name;
        this.description = description;
        this.user = user;
    }

    /**
     * Constructs a new Deck with the given name, description, owner, and list of cards.
     *
     * @param name        The name of the deck.
     * @param description A brief description about the deck's purpose or content.
     * @param user        The User who created this deck.
     * @param cards       The list of Card objects that make up this deck.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public Deck(String name, String description, User user, List<Card> cards) {
        this.name = name;
        this.description = description;
        this.user = user;
        this.cards = cards;
    }

    /**
     * Retrieves the unique identifier for this deck.
     *
     * @return The ID of the deck.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this deck. This is typically assigned by a database or system.
     *
     * @param id The new ID to assign to the deck.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    protected void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the ID of the user who owns this deck.
     *
     * @return The ID of the associated User.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public int getUserId() {
        return user.getId();
    }

    /**
     * Retrieves the name of the deck.
     *
     * @return The name of the deck.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a new name for this deck. This is typically used when renaming a deck via UI or API.
     *
     * @param name The new name to assign to the deck.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the description of the deck.
     *
     * @return The description text of the deck.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets a new description for the deck. This is useful when updating or adding more context about the deck.
     *
     * @param description The new description to assign to the deck.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves the list of cards in this deck.
     *
     * @return A List containing all Card objects associated with this Deck.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Sets a new list of cards for this deck. This is used when initializing or updating the deck's contents.
     *
     * @param cards The new list of Card objects to associate with this Deck.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    /**
     * Checks if this deck is bookmarked by its owner.
     *
     * @return true if the deck is bookmarked, false otherwise.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public boolean isBookmarked() {
        return isBookmarked;
    }

    /**
     * Sets whether this deck is bookmarked or not.
     *
     * @param isBookmarked The new bookmark status (true for bookmarked, false otherwise).
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public void setBookmarked(Boolean isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

    /**
     * Returns a string representation of the deck. This is typically used for debugging or logging.
     *
     * @return The name of the deck as its string representation.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    @Override
    public String toString() {
        return name;
    }
}