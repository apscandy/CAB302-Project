package com.cab302.cab302project.model.userSecQuestions;
import com.cab302.cab302project.model.user.User;



/**
 * Data Access Object (DAO) interface for managing user security questions.
 * This interface provides methods to persist, retrieve, and update a set of three
 * predefined security questions and their corresponding answers for a given user.
 *
 * <p>
 * Each user has exactly one record of security questions, which includes:
 * - Three distinct security questions (e.g., "What is your mother's maiden name?")
 * - Three answers to those questions (stored securely as hash values)
 * </p>
 */
public interface IUserSecurityQuestionDAO {
    /**
     * Creates a new entry in the database for the specified user's set of
     * three security questions and their corresponding answers.
     *
     * <p>
     * The provided {@code UserSecurityQuestion} object must already have all
     * three questions and answers populated. This method assumes that the
     * associated user is valid and properly initialized.
     * </p>
     *
     * @param question A fully populated {@link UserSecurityQuestion} object
     *                 containing all three security questions and their answers.
     */
    void createQuestion(UserSecurityQuestion question);

    /**
     * Retrieves the set of three predefined security questions and answers for a user.
     *
     * <p>
     * This method returns an immutable object that contains:
     * - The user's ID (via getUserId())
     * - All three security questions
     * - All three corresponding answers
     * </p>
     *
     * @param user The user whose security questions are being retrieved.
     * @return A {@link UserSecurityQuestion} object containing the retrieved data,
     *         or null if no such record exists for the given user.
     */
    UserSecurityQuestion getQuestions(User user);

    /**
     * Updates an existing user's set of three security questions and answers
     * in the database using the provided updated information.
     *
     * <p>
     * The provided {@code UserSecurityQuestion} object must contain all three
     * questions and answers for the associated user. This method assumes that:
     * - The user ID is already set (via getUserId())
     * - All three question-answer pairs are populated correctly
     * </p>
     *
     * @param updatedQuestion A fully populated {@link UserSecurityQuestion} object
     *                        with the updated security questions and answers.
     */
    void updateQuestions(UserSecurityQuestion updatedQuestion);
}