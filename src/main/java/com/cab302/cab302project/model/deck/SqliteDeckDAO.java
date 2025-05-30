package com.cab302.cab302project.model.deck;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.error.model.deck.*;
import com.cab302.cab302project.error.state.UserIsNullException;
import com.cab302.cab302project.model.SqliteConnection;
import com.cab302.cab302project.model.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * DAO implementation for managing decks in a SQLite database.
 * Provides methods to create, update, delete, and retrieve deck data
 * with support for soft-deletion and bookmarking features.
 *
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public final class SqliteDeckDAO implements IDeckDAO {

    private final Logger logger = LogManager.getLogger(SqliteDeckDAO.class);
    private final Connection con;

    // SQL statements for database operations
    private final String createDeckSQL = "INSERT INTO deck (user_id, name, description) VALUES (?,?,?)";
    private final String updateDeckSQL = "UPDATE deck SET name = ?, description = ? WHERE id = ?";
    private final String deleteDeckSQL = "DELETE FROM deck WHERE id = ?";
    private final String softDeleteDeckSQL = "UPDATE deck SET is_deleted = ? WHERE id = ?";
    private final String selectDeckSQL = "SELECT * FROM deck WHERE user_id = ? AND  is_deleted = FALSE";
    private final String selectDeckByIdSQL = "SELECT * FROM deck WHERE id = ? AND is_deleted = FALSE";
    private final String getSoftDeleteDeckSQL = "SELECT * FROM deck WHERE user_id = ? AND is_deleted = TRUE";
    private final String updateBookmarkSQL = "UPDATE deck SET is_bookmarked = ? WHERE id = ?";
    private final String selectBookmarkedDeckSQL = "SELECT * FROM deck WHERE user_id = ? AND is_bookmarked = TRUE AND is_deleted = FALSE";

    /**
     * Constructor that initializes the database connection.
     */
    public SqliteDeckDAO() {
        con = SqliteConnection.getInstance();
    }

    /**
     * Creates a new deck in the database with the specified details.
     *
     * @param deck The Deck object to be created
     * @throws DeckIsNullException if the deck is null or has an invalid user ID
     * @throws FailedToCreateDeckException if the operation fails due to SQL error
     */
    @Override
    public void createDeck(Deck deck) {
        if (deck == null || deck.getUserId() == 0) {
            throw new DeckIsNullException("Deck cannot be null");
        }
        try {
            con.setAutoCommit(false);
            try (PreparedStatement stmt = con.prepareStatement(createDeckSQL)) {
                stmt.setInt(1, deck.getUserId());
                stmt.setString(2, deck.getName());
                stmt.setString(3, deck.getDescription());
                stmt.executeUpdate();
                ResultSet resultSet = stmt.getGeneratedKeys();
                if (resultSet.next()) {
                    deck.setId(resultSet.getInt(1));
                }
                con.commit();
                stmt.close();
                resultSet.close();
            } catch (SQLException e) {
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToCreateDeckException(e.getMessage());
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToCreateDeckException(e.getMessage());
        }
    }

    /**
     * Updates an existing deck in the database with new details.
     *
     * @param deck The Deck object containing updated information
     * @throws DeckIsNullException if the deck is null or has an invalid user ID
     * @throws FailedToUpdateDeckException if the operation fails due to SQL error
     */
    @Override
    public void updateDeck(Deck deck) {
        if (deck == null || deck.getUserId() == 0) {
            throw new DeckIsNullException("Deck cannot be null");
        }
        try {
            con.setAutoCommit(false);
            try (PreparedStatement insertStatement = con.prepareStatement(updateDeckSQL)) {
                insertStatement.setString(1, deck.getName());
                insertStatement.setString(2, deck.getDescription());
                insertStatement.setInt(3, deck.getId());
                insertStatement.executeUpdate();
                con.commit();
                insertStatement.close();
            } catch (SQLException e) {
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToUpdateDeckException(e.getMessage());
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToUpdateDeckException(e.getMessage());
        }
    }

    /**
     * Permanently deletes a deck from the database.
     *
     * @param deck The Deck object to be deleted
     * @throws DeckIsNullException if the deck is null or has an invalid user ID
     * @throws FailedToDeleteDeckException if the operation fails due to SQL error
     */
    @Override
    public void deleteDeck(Deck deck) {
        if (deck == null || deck.getUserId() == 0) {
            throw new DeckIsNullException("Deck cannot be null");
        }
        try {
            con.setAutoCommit(false);
            try (PreparedStatement deleteStatement = con.prepareStatement(deleteDeckSQL)) {
                deleteStatement.setInt(1, deck.getId());
                deleteStatement.executeUpdate();
                con.commit();
                deleteStatement.close();
            } catch (SQLException e) {
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToDeleteDeckException(e.getMessage());
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToDeleteDeckException(e.getMessage());
        }
    }

    /**
     * Soft-deletes a deck by marking it as deleted in the database.
     *
     * <p>
     * This operation sets the is_deleted flag to true, effectively hiding
     * the deck from regular views while keeping the record in the database for
     * potential recovery through the recycle bin feature. It uses transactions
     * to ensure data integrity and will roll back on failure.
     * </p>
     *
     * @param deck The Deck object to be soft-deleted, must not be null and have valid ID
     * @throws DeckIsNullException if deck is null or has invalid ID
     * @throws FailedToDeleteDeckException if database operation fails
     */
    @Override
    public void softDeleteDeck(Deck deck) {
        if (deck == null || deck.getUserId() == 0 || deck.getId() == 0) {
            throw new DeckIsNullException("Deck cannot be null");
        }
        try {
            con.setAutoCommit(false);
            try (PreparedStatement softDelete = con.prepareStatement(softDeleteDeckSQL)) {
                softDelete.setBoolean(1, true);
                softDelete.setInt(2, deck.getId());
                softDelete.executeUpdate();
                con.commit();
                softDelete.close();
            } catch (SQLException e) {
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToDeleteDeckException(e.getMessage());

            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToDeleteDeckException(e.getMessage());
        }
    }

    /**
     * Restores a previously soft-deleted deck by marking it as not deleted.
     *
     * <p>
     * This operation sets the is_deleted flag to false, making the deck
     * visible again in the application. It uses transaction management to
     * ensure the restore process completes successfully and handles errors
     * gracefully.
     * </p>
     *
     * @param deck The Deck object to be restored, must not be null and have valid ID
     * @throws DeckIsNullException if deck is null or has invalid ID
     * @throws FailedToDeleteDeckException if database operation fails
     */
    @Override
    public void restoreDeck(Deck deck) {
        if (deck == null || deck.getUserId() == 0 || deck.getId() == 0) {
            throw new DeckIsNullException("Deck cannot be null");
        }
        try {
            con.setAutoCommit(false);
            try (PreparedStatement softDelete = con.prepareStatement(softDeleteDeckSQL)) {
                softDelete.setBoolean(1, false);
                softDelete.setInt(2, deck.getId());
                softDelete.executeUpdate();
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToDeleteDeckException(e.getMessage());

            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToDeleteDeckException(e.getMessage());
        }
    }

    /**
     * Retrieves all decks owned by a specific user.
     *
     * <p>
     * This method queries the database for all decks associated with the provided
     * user, filtering out any soft-deleted decks. It returns a list of Deck objects,
     * each initialized with the user's information and populated from the result set.
     * </p>
     *
     * @param user The User object representing the owner whose decks are to be retrieved
     * @return List of Deck objects owned by the specified user, excluding soft-deleted decks
     * @throws DeckIsNullException if user is null or has an invalid ID
     * @throws FailedToGetListOfDecksException if there's a database retrieval error
     */
    @Override
    public List<Deck> getDecks(User user) {
        List<Deck> decks = new ArrayList<>();
        if (user == null || user.getId() == 0) {
            throw new DeckIsNullException("Deck cannot be null");
        }
        try{
            con.setAutoCommit(false);
            try (PreparedStatement selectStatement = con.prepareStatement(selectDeckSQL)) {
                selectStatement.setInt(1, user.getId());
                ResultSet resultSet = selectStatement.executeQuery();
                while (resultSet.next()) {
                    int deckId = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    boolean bookmarked = resultSet.getBoolean("is_bookmarked");
                    Deck deck = new Deck(name, description, user);
                    deck.setId(deckId);
                    deck.setBookmarked(bookmarked);
                    decks.add(deck);
                }
                con.commit();
                selectStatement.close();
                resultSet.close();
            }catch (SQLException e) {
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToGetListOfDecksException(e.getMessage());
            }finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToGetListOfDecksException(e.getMessage());
        }
        return decks;
    }

    /**
     * Retrieves all soft-deleted decks for a specific user.
     *
     * <p>
     * This method queries the database for decks that have been marked as deleted
     * (is_deleted = true) and belong to the specified user. It returns a list of Deck
     * objects representing the soft-deleted items that can be restored or permanently
     * deleted.
     * </p>
     *
     * @param user The User object whose soft-deleted decks are being retrieved
     * @return List of Deck objects that have been soft-deleted and belong to the specified user
     * @throws UserIsNullException if user is null or has an invalid ID
     * @throws FailedToGetListOfDecksException if there's a database retrieval error
     */
    @Override
    public List<Deck> getSoftDeletedDecks(User user) {
        List<Deck> decks = new ArrayList<>();
        if (user == null || user.getId() == 0) {
            throw new UserIsNullException("User cannot be null");
        }
        try{
            con.setAutoCommit(false);
            try (PreparedStatement selectStatement = con.prepareStatement(getSoftDeleteDeckSQL)) {
                selectStatement.setInt(1, user.getId());
                ResultSet resultSet = selectStatement.executeQuery();
                while (resultSet.next()) {
                    int deckId = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    Deck deck = new Deck(name, description, user);
                    deck.setId(deckId);
                    decks.add(deck);
                }
                con.commit();
                selectStatement.close();
                resultSet.close();
            }catch (SQLException e){
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToGetListOfDecksException(e.getMessage());
            }finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToGetListOfDecksException(e.getMessage());
        }
        return decks;
    }

    /**
     * Retrieves a specific deck by its ID.
     *
     * <p>
     * This method queries the database for a deck with the given ID and returns
     * the corresponding Deck object. If no deck is found, it returns null.
     * </p>
     *
     * @param id The unique identifier of the deck to retrieve
     * @return The Deck object associated with the provided ID, or null if not found
     * @throws DeckIsNullException if the ID is invalid (less than or equal to zero)
     * @throws FailedToGetDeckException if database retrieval fails
     */
    @Override
    public Deck getDeck(int id) {
        Deck deck = null;
        if (id <= 0) {
            throw new DeckIsNullException("Deck cannot be null");
        }
        try{
            con.setAutoCommit(false);
            try( PreparedStatement selectStatement = con.prepareStatement(selectDeckByIdSQL)) {
                selectStatement.setInt(1, id);
                ResultSet resultSet = selectStatement.executeQuery();
                if (resultSet.next()) {
                    int deckId = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    boolean bookmarked = resultSet.getBoolean("is_bookmarked");
                    deck = new Deck(name, description, ApplicationState.getCurrentUser());
                    deck.setId(deckId);
                    deck.setBookmarked(bookmarked);
                }
                con.commit();
                selectStatement.close();
                resultSet.close();
            } catch (SQLException  e) {
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToGetDeckException(e.getMessage());
            }finally {
                con.setAutoCommit(true);
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToGetDeckException(e.getMessage());
        }
        return deck;
    }

    /**
     * Sets the bookmark status of a specific deck.
     *
     * <p>
     * Updates the is_bookmarked flag in the database for the given deck. This action
     * marks the deck as either bookmarked or unbookmarked, depending on the provided
     * boolean value. The operation is performed using an SQL UPDATE statement to
     * ensure data consistency and integrity.
     * </p>
     *
     * @param deck The deck whose bookmark status is being updated
     * @param bookmarked The new bookmark status (true for bookmarked, false otherwise)
     * @throws DeckIsNullException if the provided deck is null or has an invalid ID
     * @throws FailedToUpdateDeckException if database operation fails
     */
    @Override
    public void setBookmarked(Deck deck, boolean bookmarked) {
        if (deck == null || deck.getUserId() == 0) {
            throw new DeckIsNullException("Deck cannot be null");
        }
        try (PreparedStatement stmt = con.prepareStatement(updateBookmarkSQL)) {
            stmt.setBoolean(1, bookmarked);
            stmt.setInt(2, deck.getId());
            stmt.executeUpdate();
            deck.setBookmarked(bookmarked);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new FailedToUpdateDeckException(e.getMessage());
        }
    }

    /**
     * Retrieves all decks that are currently bookmarked by a specific user.
     *
     * <p>
     * This method queries the database for all decks marked as bookmarked
     * (is_bookmarked = true) and owned by the specified user. It returns them
     * in a list of Deck objects, which can be used to display favorite or
     * frequently accessed decks within the application interface.
     * </p>
     *
     * @param user The user whose bookmarked decks are being retrieved
     * @return List of bookmarked Deck objects belonging to the user
     * @throws DeckIsNullException if the provided user is null or has an invalid ID
     * @throws FailedToGetListOfDecksException if database retrieval fails
     */
    @Override
    public List<Deck> getBookmarkedDecks(User user) {
        List<Deck> decks = new ArrayList<>();
        if (user == null || user.getId() == 0) {
            throw new DeckIsNullException("User cannot be null");
        }
        try {
            con.setAutoCommit(false);
            try (PreparedStatement stmt = con.prepareStatement(selectBookmarkedDeckSQL)) {
                stmt.setInt(1, user.getId());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int deckId        = rs.getInt("id");
                    String name       = rs.getString("name");
                    String description= rs.getString("description");

                    Deck deck = new Deck(name, description, user);
                    deck.setId(deckId);
                    deck.setBookmarked(true);
                    decks.add(deck);
                }
                con.commit();
                rs.close();
            } catch (SQLException e) {
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToGetListOfDecksException(e.getMessage());
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToGetListOfDecksException(e.getMessage());
        }
        return decks;
    }
}
