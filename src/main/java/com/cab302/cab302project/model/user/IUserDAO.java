package com.cab302.cab302project.model.user;

/**
 * Interface defining the Data Access Object (DAO) operations for managing user data in the application.
 * This interface abstracts database interactions related to user entities, allowing for separation of concerns
 * between business logic and data persistence layers. Implementations should handle CRUD operations
 * (Create, Read, Update, Delete) for User objects.
 *
 * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
 */
public interface IUserDAO {
    /**
     * Adds a new user to the database.
     *
     * @param user The User object representing the user to be added. This object must contain valid
     *             first name, last name, email, and password fields.
     */
    void addUser(User user);

    /**
     * Updates an existing user in the database with the provided details.
     *
     * @param user The User object containing updated information. The ID of this user must already exist
     *             in the database to ensure successful update.
     */
    void updateUser(User user);

    /**
     * Retrieves a user from the database by their unique identifier (ID).
     *
     * @param id The numeric ID of the user to retrieve.
     * @return The User object corresponding to the provided ID, or null if no such user exists.
     */
    User getUser(int id);

    /**
     * Retrieves a user from the database by their email address.
     *
     * @param email The email address of the user to retrieve. This must match exactly with an existing
     *              user's email in the database.
     * @return The User object corresponding to the provided email, or null if no such user exists.
     */
    User getUser(String email);
}
