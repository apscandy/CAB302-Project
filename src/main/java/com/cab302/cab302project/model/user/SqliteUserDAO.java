package com.cab302.cab302project.model.user;

import com.cab302.cab302project.error.model.user.FailedToCreateUserException;
import com.cab302.cab302project.error.model.user.FailedToGetUserException;
import com.cab302.cab302project.error.model.user.FailedToUpdateUserException;
import com.cab302.cab302project.model.SqliteConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of IUserDAO for SQLite.
 * This class handles all user-related database operations, including
 * creating, updating, retrieving and deleting user records.
 * Operations are executed within transactions to ensure consistency and
 * integrity. Custom exceptions are thrown for specific failure scenarios.
 *
 * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
 */
public final class SqliteUserDAO implements IUserDAO {

    private final Connection con;
    private static final Logger logger = LogManager.getLogger(SqliteUserDAO.class);

    /**
     * Constructs a new SqliteUserDAO using a singleton SQLite connection.
     */
    public SqliteUserDAO() {
        con = SqliteConnection.getInstance();
    }

    private final String addUserSQL = "INSERT INTO user (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
    private final String updateUserSQL = "UPDATE user SET first_name = ?, last_name = ?, email = ?, password = ? WHERE id = ?";
    private final String getUserByIdSQL = "SELECT * FROM user WHERE id = ?";
    private final String getUserByEmailSQL = "SELECT * FROM user WHERE email = ?";

    /**
     * Adds a new user to the database.
     * The user must have a non-null first name, last name, and email.
     * A transaction is used to ensure the operation is atomic.
     *
     * @param user the user to be added
     * @throws FailedToCreateUserException if the insertion fails
     * @throws IllegalArgumentException if required user fields are null
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Override
    public void addUser(User user) {
        if (user == null || user.getFirstName() == null || user.getLastName() == null || user.getEmail() == null) {
            throw new IllegalArgumentException();
        }
        try {
            try {
                con.setAutoCommit(false);
                PreparedStatement sql = con.prepareStatement(addUserSQL);
                sql.setString(1, user.getFirstName());
                sql.setString(2, user.getLastName());
                sql.setString(3, user.getEmail());
                sql.setString(4, user.getPassword());
                sql.executeUpdate();
                ResultSet result = sql.getGeneratedKeys();
                if (result.next()) {
                    user.setId(result.getInt(1));
                }
                con.commit();
                sql.close();
                result.close();
            } catch (SQLException e) {
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToCreateUserException(e.getMessage());
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToCreateUserException(e.getMessage());
        }
    }

    /**
     * Updates an existing user's information in the database.
     * Executes within a transaction for consistency.
     *
     * @param user the user object with updated information
     * @throws FailedToUpdateUserException if update fails
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Override
    public void updateUser(User user) {
        try {
            con.setAutoCommit(false);
            try {
                PreparedStatement sql = con.prepareStatement(updateUserSQL);
                sql.setString(1, user.getFirstName());
                sql.setString(2, user.getLastName());
                sql.setString(3, user.getEmail());
                sql.setString(4, user.getPassword());
                sql.setInt(5, user.getId());
                sql.executeUpdate();
                con.commit();
                sql.close();
            } catch (SQLException e) {
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToUpdateUserException(e.getMessage());
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToUpdateUserException(e.getMessage());
        }
    }

    /**
     * Retrieves a user from the database by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return the User object if found, null otherwise
     * @throws FailedToGetUserException if the retrieval fails
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Override
    public User getUser(int id) {
        User user = null;
        try {
            con.setAutoCommit(false);
            try (PreparedStatement sql = con.prepareStatement(getUserByIdSQL)) {
                sql.setInt(1, id);
                ResultSet result = sql.executeQuery();
                if (result.next()) {
                    String firstName = result.getString("first_name");
                    String lastName = result.getString("last_name");
                    String password = result.getString("password");
                    String emails = result.getString("email");
                    int ids = result.getInt("id");
                    user = new User(firstName, lastName, emails, password);
                    user.setId(ids);
                }
                con.commit();
                sql.close();
                result.close();
            } catch (SQLException e) {
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToGetUserException(e.getMessage());
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToGetUserException(e.getMessage());
        }
        return user;
    }

    /**
     * Retrieves a user from the database by their email address.
     *
     * @param email the email of the user to retrieve
     * @return the User object if found, null otherwise
     * @throws FailedToGetUserException if the retrieval fails
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Override
    public User getUser(String email) {
        User user = null;
        try {
            con.setAutoCommit(false);
            try (PreparedStatement sql = con.prepareStatement(getUserByEmailSQL)) {
                sql.setString(1, email);
                ResultSet result = sql.executeQuery();
                if (result.next()) {
                    String firstName = result.getString("first_name");
                    String lastName = result.getString("last_name");
                    String password = result.getString("password");
                    String emails = result.getString("email");
                    int id = result.getInt("id");
                    user = new User(firstName, lastName, emails, password);
                    user.setId(id);
                }
                con.commit();
                sql.close();
                result.close();
            } catch (SQLException e) {
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToGetUserException(e.getMessage());
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToGetUserException(e.getMessage());
        }
        return user;
    }

    /**
     * Deletes a user record from the database based on their unique ID.
     *
     * <p>
     * This method executes a SQL DELETE statement to remove the specified user from the "user" table,
     * using the user's ID as the identifier. If the deletion fails due to a SQL exception,
     * a custom {@link FailedToUpdateUserException} is thrown and the error is logged.
     * </p>
     *
     * @param user The {@link User} object to be deleted. Must not be {@code null}, and must have a valid ID.
     * @throws FailedToUpdateUserException if the database deletion fails.
     *
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    public void deleteUser(User user) {
        String deleteSQL = "DELETE FROM user WHERE id = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(deleteSQL);
            stmt.setInt(1, user.getId());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new FailedToUpdateUserException(e.getMessage());
        }
    }
}
