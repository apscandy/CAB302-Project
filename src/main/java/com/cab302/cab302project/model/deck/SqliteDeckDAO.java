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

public class SqliteDeckDAO implements IDeckDAO {

    private final Logger logger = LogManager.getLogger(SqliteDeckDAO.class);
    private final Connection con;

    private final String createDeckSQL = "INSERT INTO deck (user_id, name, description) VALUES (?,?,?)";
    private final String updateDeckSQL = "UPDATE deck SET name = ?, description = ? WHERE id = ?";
    private final String deleteDeckSQL = "DELETE FROM deck WHERE id = ?";
    private final String softDeleteDeckSQL = "UPDATE deck SET is_deleted = ?, WHERE id = ?";
    private final String selectDeckSQL = "SELECT * FROM deck WHERE user_id = ? AND  is_deleted = FALSE";
    private final String selectDeckByIdSQL = "SELECT * FROM deck WHERE id = ? AND is_deleted = FALSE";
    private final String getSoftDeleteDeckSQL = "SELECT * FROM deck WHERE user_id = ? AND  is_deleted = TRUE";

    public SqliteDeckDAO() {
        con = SqliteConnection.getInstance();
    }


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
                throw new FailedToDeleteDeckException(e.getMessage());
            }finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToCreateDeckException(e.getMessage());
        }
    }


    @Override
    public void updateDeck(Deck deck)  {
        if (deck == null || deck.getUserId() == 0) {
            throw new DeckIsNullException("Deck cannot be null");
        }
        try {
            // Transaction try/catch block
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
                throw new FailedToDeleteDeckException(e.getMessage());
            }finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToUpdateDeckException(e.getMessage());
        }
    }

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

    /*
    TODO: look are replacing user with ApplicationState.getCurrentUser()
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
                    Deck deck = new Deck(name, description, user);
                    deck.setId(deckId);
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
                    int deckId = resultSet.getInt("deck_id");
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
                    deck = new Deck(name, description, ApplicationState.getCurrentUser());
                    deck.setId(deckId);
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
}
