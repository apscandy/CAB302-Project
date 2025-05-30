package com.cab302.cab302project.model.session;

import com.cab302.cab302project.model.card.Card;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Represents the results of a user's attempt to answer a card during a session.
 * This class holds information about the session, the card being tested,
 * and whether the answer was correct or incorrect along with the time taken.
 */
public final class SessionResults {


    /**
     * The session associated with this result.
     * This field is immutable once set in the constructor.
     */
    private final Session session;

    /**
     * The card that was being tested during this attempt.
     * This field is immutable once set in the constructor.
     */
    private final Card card;

    /**
     * Indicates whether the user's answer was correct.
     * <p>
     * Note: This flag is mutually exclusive with {@link #incorrect}.
     * Only one of these should be true at a time to represent the result.
     */
    private Boolean correct = false;

    /**
     * Indicates whether the user's answer was incorrect.
     * <p>
     * Note: This flag is mutually exclusive with {@link #correct}.
     * Only one of these should be true at a time to represent the result.
     */
    private Boolean incorrect = false;

    /**
     * The time taken (in seconds) to provide an answer, calculated between
     * the start and end times of the attempt. If not set, this value remains 0.
     */
    private long timeToAnswer = 0;

    /**
     * Constructs a new SessionResults object with the specified session and card.
     *
     * @param session the session associated with this result
     * @param card    the card being tested in this attempt
     */
    public SessionResults(Session session, Card card) {
        this.session = session;
        this.card = card;
    }

    /**
     * Returns the unique identifier of the session associated with this result.
     *
     * @return the session ID
     */
    public int getId() {
        return session.getId();
    }

    /**
     * Returns the unique identifier of the card being tested in this attempt.
     *
     * @return the card ID
     */
    public int getCardId() {
        return card.getId();
    }

    /**
     * Returns whether the user's answer was correct.
     *
     * @return true if the answer was correct, false otherwise
     */
    public Boolean getCorrect() {
        return correct;
    }

    /**
     * Sets whether the user's answer was correct.
     *
     * @param correct true if the answer was correct, false otherwise
     */
    void setCorrect(boolean correct) {
        this.correct = correct;
    }

    /**
     * Returns whether the user's answer was incorrect.
     *
     * @return true if the answer was incorrect, false otherwise
     */
    public Boolean getIncorrect() {
        return incorrect;
    }


    /**
     * Sets whether the user's answer was incorrect.
     *
     * @param incorrect true if the answer was incorrect, false otherwise
     */
    void setIncorrect(boolean incorrect) {
        this.incorrect = incorrect;
    }


    /**
     * Calculates and sets the time taken to answer in seconds between the start
     * and end times of the attempt. This method assumes that the end time is
     * after the start time; if not, the result may be negative or undefined.
     *
     * @param startTime the start time of the attempt
     * @param endTime   the end time of the attempt
     */
    public void setTimeToAnswer(LocalDateTime startTime, LocalDateTime endTime) {
        this.timeToAnswer = Duration.between(startTime, endTime).toSeconds();
    }

    /**
     * Returns the time taken to answer as a formatted string representing seconds.
     *
     * @return the time in seconds as a String (e.g., "123")
     */
    public String getTimeToAnswer() {
        return String.format("%d", timeToAnswer);
    }
}
