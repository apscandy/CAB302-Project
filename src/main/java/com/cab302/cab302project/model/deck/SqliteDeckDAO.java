package com.cab302.cab302project.model.deck;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.model.SqliteConnection;
import com.cab302.cab302project.model.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteDeckDAO implements IDeckDAO {

    private static final Logger logger = LogManager.getLogger(SqliteDeckDAO.class);
    private final Connection con;

    private final String createDeckSQL = "INSERT INTO deck (user_id, name, description) VALUES (?,?,?)";
    private final String updateDeckSQL = "UPDATE deck SET name = ?, description = ? WHERE id = ?";
    private final String deleteDeckSQL = "DELETE FROM deck WHERE id = ?";
    private final String selectDeckSQL = "SELECT * FROM deck WHERE user_id = ?";
    private final String selectDeckByIdSQL = "SELECT * FROM deck WHERE id = ?";

    public SqliteDeckDAO() {
        con = SqliteConnection.getInstance();
    }


    @Override
    public void createDeck(Deck deck) {
        if (deck == null || deck.getUserId() == 0) {
            logger.error("Deck is null or empty {createDeck}");
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
                logger.info("Created Deck transaction completed successfully.");
            } catch (SQLException  e) {
                con.rollback();
                logger.error("Created Deck transaction failed.");
                logger.fatal(e.getMessage());
            }finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.fatal(e.getMessage());
        }
    }


    @Override
    public void updateDeck(Deck deck) {
        if (deck == null || deck.getUserId() == 0) {
            logger.error("Deck is null or empty {updateDeck}");
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
                logger.info("Update Deck transaction completed successfully.");
            }catch (SQLException  e) {
                con.rollback();
                logger.error("Update Deck transaction failed.");
                logger.fatal(e.getMessage());
            }finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.fatal(e.getMessage());
        }
    }

    @Override
    public void deleteDeck(Deck deck) {
        if (deck == null || deck.getUserId() == 0) {
            logger.error("Deck is null or empty {deleteDeck}");
        }
        if (deck.getId() <= 0) {
            logger.error("Deck ID is less than or equal to 0 {deleteDeck}");
        }
       try{
           // Transaction try/catch block
           con.setAutoCommit(false);
           try (PreparedStatement deleteStatement = con.prepareStatement(deleteDeckSQL)) {
               deleteStatement.setInt(1, deck.getId());
               deleteStatement.executeUpdate();
               con.commit();
               logger.info("Delete Deck transaction completed successfully.");
           }catch (SQLException  e) {
               con.rollback();
               logger.error("Delete Deck transaction failed.");
               logger.fatal(e.getMessage());
           }finally {
               con.setAutoCommit(true);
           }
       } catch (Exception e) {
           logger.fatal(e.getMessage());
       }
    }

    /*
    TODO: look are replacing user with ApplicationState.getCurrentUser()
     */
    @Override
    public List<Deck> getDecks(User user) {
        List<Deck> decks = new ArrayList<>();
        if (user == null || user.getId() == 0) {
            logger.error("User is null or empty {getDecks}");
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
                resultSet.close();
                con.commit();
                logger.info("Get Decks transaction completed successfully.");
            }catch (SQLException e) {
                con.rollback();
                logger.error("Get Decks Deck transaction failed.");
                logger.fatal(e.getMessage());
            }finally {
                con.setAutoCommit(true);

            }
        } catch (Exception e) {
            logger.fatal(e.getMessage());
        }
        return decks;
    }

    @Override
    public Deck getDeck(int id) {
        Deck deck = null;
        if (id <= 0) {
            logger.error("id is less then or equal to 0 {getDeck}");
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
                logger.info("Get Deck transaction completed successfully.");
            } catch (SQLException  e) {
                con.rollback();
                logger.error("Get Deck Deck transaction failed.");
                logger.fatal(e.getMessage());
            }finally {
                con.setAutoCommit(true);
            }
        }catch (Exception e) {
            logger.fatal(e.getMessage());
        }
        return deck;
    }
}
