package com.cab302.cab302project.model.card;

import com.cab302.cab302project.error.model.card.FailedToCreateCardException;
import com.cab302.cab302project.error.model.card.FailedToGetCardsException;
import com.cab302.cab302project.error.model.card.FailedToSoftDeleteCardException;
import com.cab302.cab302project.error.model.card.FailedToUpdateCardException;
import com.cab302.cab302project.model.SqliteConnection;
import com.cab302.cab302project.model.deck.Deck;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of ICardDAO for SQLite.
 * Handles insertion, update, and soft deletion of cards, as well as retrieval of cards for a given deck.
 *
 * @author Monica Borg (n9802045)
 */
public final class SqliteCardDAO implements ICardDAO {

    private final Logger logger = LogManager.getLogger(this.getClass());

    private final Connection con;

    private final String insertCardSQL = "INSERT INTO card (deck_id, question, answer, tags) VALUES (?, ?, ?, ?)";
    private final String updateCardSQL = "UPDATE card SET question = ?, answer = ?, tags = ? WHERE id = ?";
    private final String softDeleteSQL = "UPDATE card SET is_deleted = true WHERE id = ?";
    private final String getCardsForDeckSQL = "SELECT * FROM card WHERE deck_id = ? AND is_deleted = 0";

    /**
     * Constructs a new SqliteCardDAO, obtaining a connection from SqliteConnection.
     */
    public SqliteCardDAO() {
        this.con = SqliteConnection.getInstance();
    }

    /**
     * Adds a card to the database.
     * Uses a transaction to ensure the operation is atomic.
     *
     * @param card the card to add
     * @throws RuntimeException if an error occurs during insertion
     */
    @Override
    public void addCard(Card card) throws RuntimeException {
        try{
            con.setAutoCommit(false);
            try (PreparedStatement insertStatement = con.prepareStatement(insertCardSQL)) {
                insertStatement.setInt(1, card.getDeck().getId());
                insertStatement.setString(2, card.getQuestion());
                insertStatement.setString(3, card.getAnswer());
                insertStatement.setString(4, card.getTags());
                insertStatement.executeUpdate();
                con.commit();
                insertStatement.close();
            }catch (SQLException e) {
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToCreateCardException(e.getMessage());
            }finally {
                con.setAutoCommit(true);
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToCreateCardException(e.getMessage());
        }
    }

    /**
     * Updates an existing card in the database.
     * Uses a transaction to ensure the operation is atomic.
     *
     * @param card the card to update
     */
    @Override
    public void updateCard(Card card) {
        try{
            con.setAutoCommit(false);
            try (PreparedStatement updateStatement = con.prepareStatement(updateCardSQL)) {
                updateStatement.setInt(1, card.getDeck().getId());
                updateStatement.setString(2, card.getQuestion());
                updateStatement.setString(3, card.getAnswer());
                updateStatement.setString(4, card.getTags());
                updateStatement.setInt(5, card.getId());
                updateStatement.executeUpdate();
                con.commit();
                updateStatement.close();
            }catch (SQLException e) {
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToUpdateCardException(e.getMessage());
            }finally {
                con.setAutoCommit(true);
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToUpdateCardException();
        }
    }

    /**
     * Soft deletes a card from the database by setting its is_deleted flag.
     * Uses a transaction to ensure the operation is atomic.
     *
     * @param card the card to soft delete
     */
    @Override
    public void softDeleteCard(Card card) {
        try {
            con.setAutoCommit(false);
            try (PreparedStatement softDeleteStatement = con.prepareStatement(softDeleteSQL)) {
                softDeleteStatement.setInt(1, card.getId());
                softDeleteStatement.executeUpdate();
                con.commit();
                softDeleteStatement.close();
            }catch (SQLException e) {
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToSoftDeleteCardException(e.getMessage());
            }finally {
                con.setAutoCommit(true);
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToSoftDeleteCardException(e.getMessage());
        }
    }

    /**
     * Retrieves all non-deleted cards for the given deck.
     *
     * @param deck the deck for which cards are retrieved
     * @return a List of cards in the deck
     */
    @Override
    public List<Card> getCardsForDeck(Deck deck) {
        List<Card> cards = new ArrayList<>();
        try {
            con.setAutoCommit(false);
            try (PreparedStatement getCardStatment = con.prepareStatement(getCardsForDeckSQL)) {
                getCardStatment.setInt(1, deck.getId());
                ResultSet rs = getCardStatment.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String question = rs.getString("question");
                    String answer = rs.getString("answer");
                    String tags = rs.getString("tags");
                    Card card = new Card(deck, question, answer, tags);
                    card.setId(id);
                    cards.add(card);
                }
                con.commit();
                getCardStatment.close();
                rs.close();
            } catch (SQLException ex) {
                con.rollback();
                logger.error(ex.getMessage());
                throw new FailedToGetCardsException(ex.getMessage());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToGetCardsException(e.getMessage());
        }
        return cards;
    }


    /**
     * I Created this class to utilise the full power of encapsulation
     * by passing a deck and setting to into the deck object
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     * @param deck Deck to load card into
     */
    @Override
    public void getCardAndLoadIntoDeck(Deck deck) {
        List<Card> cards = new ArrayList<>();
        try {
            con.setAutoCommit(false);
            try (PreparedStatement getCardStatment = con.prepareStatement(getCardsForDeckSQL)) {
                getCardStatment.setInt(1, deck.getId());
                ResultSet rs = getCardStatment.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String question = rs.getString("question");
                    String answer = rs.getString("answer");
                    String tags = rs.getString("tags");
                    Card card = new Card(deck, question, answer, tags);
                    card.setId(id);
                    if (card.getId() != 0) cards.add(card);
                }
                deck.setCards(cards);
                con.commit();
                getCardStatment.close();
                rs.close();
            } catch (SQLException ex) {
                con.rollback();
                logger.error(ex.getMessage());
                throw new FailedToGetCardsException(ex.getMessage());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToGetCardsException(e.getMessage());
        }
    }



    @Override
    public void softDeleteCardsByDeck(Deck deck) {
        throw new RuntimeException("Not implemented");
    }
}
