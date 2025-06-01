package com.cab302.cab302project;


/**
 * Enumerates the different test modes available for the flash card application.
 * This determines how flashcards are presented and ordered during testing sessions.
 *
 * <p>Each mode defines a distinct strategy for navigating through the flashcard set,
 * balancing predictability, randomness, and adaptive learning approaches.</p>
 *
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public enum TestModes {
    /**
     * Tests flashcards in the order they are stored in the collection.
     * This mode is ideal for sequential review and memorization of cards in a fixed sequence.
     * <p>Example: Card 1 → Card 2 → Card 3 ...</p>
     */
    SEQUENTIAL,

    /**
     * Tests flashcards in a randomized order each session.
     * This mode helps prevent pattern recognition by presenting cards unpredictably,
     * making it suitable for varied practice and retention testing.
     * <p>Example: Cards are shuffled randomly before each test session.</p>
     */
    RANDOM,

    /**
     * Tests flashcards using an adaptive, intelligent strategy.
     * This mode prioritizes cards based on performance metrics (e.g., difficulty, error rates)
     * to optimize learning efficiency. It dynamically adjusts the order to focus more on challenging cards.
     * <p>Example: Cards with higher error rates are repeated more frequently.</p>
     */
    SMART
}
