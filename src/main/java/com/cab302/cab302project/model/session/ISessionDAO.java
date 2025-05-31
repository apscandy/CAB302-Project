package com.cab302.cab302project.model.session;

import java.time.LocalDateTime;

/**
 * Interface defining methods for persisting session and session result data in a persistent storage (e.g., database).
 * This interface abstracts the data access layer for sessions and their associated results.
 */
public interface ISessionDAO {

    /**
     * Persists a new session to the database.
     *
     * @param session The Session object representing the user's interaction with a deck.
     *                This includes details like deck ID, user ID, start time, and test mode.
     */
    void createSession(Session session);

    /**
     * Ends an active session by updating its end time and marking it as completed.
     *
     * @param session The Session object to be ended. This method will set the endDateTime
     *                to the current moment and mark the session as finished.
     */
    void endSession(Session session);

    /**
     * Persists a session result for a specific card attempt to the database.
     *
     * @param sessionResults The SessionResults object containing details of the user's answer,
     *                       including the associated session, card, correctness status, and time taken.
     */
    void createSessionResult(SessionResults sessionResults);

    /**
     * Marks a card attempt as correct and records the time taken to respond.
     *
     * @param sessionResults   The SessionResults object to be updated with correctness information.
     * @param startTime        The start time of the card attempt (e.g., when the user began answering).
     * @param endTime          The end time of the card attempt (e.g., when the user submitted their answer).
     *                         This method calculates the duration between start and end times
     *                         and sets it as the time taken for the response.
     */
    void sessionResultCardCorrect(SessionResults sessionResults, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Marks a card attempt as incorrect and records the time taken to respond.
     *
     * @param sessionResults   The SessionResults object to be updated with correctness information.
     * @param startTime        The start time of the card attempt (e.g., when the user began answering).
     * @param endTime          The end time of the card attempt (e.g., when the user submitted their answer).
     *                         This method calculates the duration between start and end times
     *                         and sets it as the time taken for the response.
     */
    void sessionResultCardIncorrect(SessionResults sessionResults, LocalDateTime startTime, LocalDateTime endTime);
}
