package com.cab302.cab302project.model.card;

import com.cab302.cab302project.model.deck.Deck;

/**
 * Represents a flashcard that belongs to a deck.
 * Each Card has an id, associated deck, question, answer, tags, and a deletion flag.
 *
 * @author Monica Borg (n9802045)
 */
public final class Card {
    private int id;
    private Deck deck;
    private String question;
    private String answer;
    private String tags;
    private boolean isDeleted = false;

    /**
     * Constructs a new Card with the specified deck, question, answer, and tags.
     *
     * @param deck     the Deck this card belongs to
     * @param question the question text of the card
     * @param answer   the answer text of the card
     * @param tags     the tags associated with the card
     */
    public Card(Deck deck, String question, String answer, String tags) {
        this.deck = deck;
        this.question = question;
        this.answer = answer;
        this.tags = tags;
    }

    /**
     * Gets the card's id.
     *
     * @return the id of the card
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the deck associated with this card.
     *
     * @return the deck of the card
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Gets the question text of this card.
     *
     * @return the question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Gets the answer text of this card.
     *
     * @return the answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Gets the tags associated with this card.
     *
     * @return the tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * Checks whether this card is marked as deleted (soft delete).
     *
     * @return true if deleted, false otherwise
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the deck of the card.
     *
     * @param deck the new deck to set
     */
    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    /**
     * Sets the question text of the card.
     *
     * @param question the new question text
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Sets the answer text of the card.
     *
     * @param answer the new answer text
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * Sets the tags of the card.
     *
     * @param tags the new tags
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    /**
     * Returns a string representation of the card.
     * Typically, the question is returned.
     *
     * @return the question text of the card
     */
    @Override
    public String toString() {
        return question;
    }
}
