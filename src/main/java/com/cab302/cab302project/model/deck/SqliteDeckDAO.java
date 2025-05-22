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
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public final class SqliteDeckDAO implements IDeckDAO {

    private final Logger logger = LogManager.getLogger(SqliteDeckDAO.class);
    private final Connection con;

    private final String createDeckSQL = "INSERT INTO deck (user_id, name, description) VALUES (?,?,?)";
    private final String updateDeckSQL = "UPDATE deck SET name = ?, description = ? WHERE id = ?";
    private final String deleteDeckSQL = "DELETE FROM deck WHERE id = ?";
    private final String softDeleteDeckSQL = "UPDATE deck SET is_deleted = ? WHERE id = ?";
    private final String selectDeckSQL = "SELECT * FROM deck WHERE user_id = ? AND  is_deleted = FALSE";
    private final String selectDeckByIdSQL = "SELECT * FROM deck WHERE id = ? AND is_deleted = FALSE";
    private final String getSoftDeleteDeckSQL = "SELECT * FROM deck WHERE user_id = ? AND is_deleted = TRUE";

    // ─── NEW SQL for bookmarking ──────────────────────────────────────────────────
    private final String updateBookmarkSQL = "UPDATE deck SET is_bookmarked = ? WHERE id = ?";
    private final String selectBookmarkedDeckSQL = "SELECT * FROM deck WHERE user_id = ? AND is_bookmarked = TRUE AND is_deleted = FALSE";
    // ──────────────────────────────────────────────────────────────────────────────

    public SqliteDeckDAO() {
        con = SqliteConnection.getInstance();
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    @Override
    public void createDeck(Deck deck) {
        if (deck == null || deck.getUserId() == 0) {
            throw new DeckIsNullException("Deck cannot be null");
        }
        try {
            // Transaction try/catch block
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
            } catch (SQLException  e) {
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
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
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
            }catch (SQLException  e) {
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
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    @Override
    public void deleteDeck(Deck deck) {
        if (deck == null || deck.getUserId() == 0) {
            throw new DeckIsNullException("Deck cannot be null");
        }
       try{
           // Transaction try/catch block
           con.setAutoCommit(false);
           try (PreparedStatement deleteStatement = con.prepareStatement(deleteDeckSQL)) {
               deleteStatement.setInt(1, deck.getId());
               deleteStatement.executeUpdate();
               con.commit();
               deleteStatement.close();
           }catch (SQLException  e) {
               con.rollback();
               logger.error(e.getMessage());
               throw new FailedToDeleteDeckException(e.getMessage());
           }finally {
               con.setAutoCommit(true);
           }
       } catch (Exception e) {
           logger.error(e.getMessage());
           throw new FailedToDeleteDeckException(e.getMessage());
       }
    }


    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    @Override
    public void softDeleteDeck(Deck deck) {
        if( deck == null || deck.getUserId() == 0 || deck.getId() == 0){
            throw new DeckIsNullException("Deck cannot be null");
        }
        try {
            con.setAutoCommit(false);
            try(PreparedStatement softDelete = con.prepareStatement(softDeleteDeckSQL)){
                softDelete.setBoolean(1, true);
                softDelete.setInt(2, deck.getId());
                softDelete.executeUpdate();
                con.commit();
                softDelete.close();
            }catch (SQLException e){
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToDeleteDeckException(e.getMessage());

            }finally {
                con.setAutoCommit(true);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new FailedToDeleteDeckException(e.getMessage());
        }
    }

    /**
     * @author Hoang Dat Bui
     */
    @Override
    public void restoreDeck(Deck deck) {
        if (deck == null || deck.getUserId() == 0 || deck.getId() == 0){
            throw new DeckIsNullException("Deck cannot be null");
        }
        try {
            con.setAutoCommit(false);
            try(PreparedStatement softDelete = con.prepareStatement(softDeleteDeckSQL)) {
                softDelete.setBoolean(1, false);
                softDelete.setInt(2, deck.getId());
                softDelete.executeUpdate();
                con.commit();
            } catch (SQLException e){
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToDeleteDeckException(e.getMessage());

            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new FailedToDeleteDeckException(e.getMessage());
        }
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
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
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    @Override
    public List<Deck> getSoftDeletedDecks(User user) {
        List<Deck> decks = new ArrayList<>();
        if (user == null || user.getId() == 0) {
            throw new UserIsNullException("Deck cannot be null");
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
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
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
