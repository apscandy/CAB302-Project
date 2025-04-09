package com.cab302.cab302project.model.card;

import com.cab302.cab302project.error.card.FailedToCreateCardException;
import com.cab302.cab302project.error.card.FailedToGetCardsException;
import com.cab302.cab302project.error.card.FailedToSoftDeleteCardException;
import com.cab302.cab302project.error.card.FailedToUpdateCardException;
import com.cab302.cab302project.model.SqliteConnection;
import com.cab302.cab302project.model.deck.Deck;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteCardDAO implements ICardDAO {

    private Logger logger = LogManager.getLogger(this.getClass());

    private final Connection con;

    private final String insertCardSQL = "INSERT INTO card (deck_id, question, answer, tags) VALUES (?, ?, ?, ?)";

    private final String updateCardSQL = "UPDATE card SET question = ?, answer = ?, tags = ? WHERE id = ?";

    private final String softDeleteSQL = "UPDATE card SET is_deleted = 1 WHERE id = ?";

    private final String softDelefByDeckSQL = "UPDATE card SET is_updated = 1 WHERE deck_id = ?";

    private final String getCardsForDeckSQL = "SELECT * FROM card WHERE deck_id = ? AND is_deleted = 0";



    public SqliteCardDAO() {
        this.con = SqliteConnection.getInstance();
    }

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
                logger.error("Failed to insert card {}", e.getMessage());
                throw new FailedToCreateCardException(e.getMessage());
            }finally {
                con.setAutoCommit(true);
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToCreateCardException(e.getMessage());
        }
    }

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
                logger.error("Failed to update card {}", e.getMessage());
                throw new FailedToUpdateCardException(e.getMessage());
            }finally {
                con.setAutoCommit(true);
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToUpdateCardException();
        }
    }

    @Override
    public void softDeleteCard(Card card) {
        try {
            con.setAutoCommit(false);
            try (PreparedStatement softDeleteStatement = con.prepareStatement(softDeleteSQL)) {

            }catch (SQLException e) {
                con.rollback();
                logger.error("Failed to delete card {}", e.getMessage());
                throw new FailedToSoftDeleteCardException(e.getMessage());
            }finally {
                con.setAutoCommit(true);
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToSoftDeleteCardException(e.getMessage());
        }
    }

    @Override
    public void softDeleteCardsByDeck(Deck deck) {
        try{
            try (PreparedStatement softDelefByDeckStatment = con.prepareStatement(softDelefByDeckSQL)) {
                softDelefByDeckStatment.setInt(1, deck.getId());
                softDelefByDeckStatment.executeUpdate();
                con.commit();
                softDelefByDeckStatment.close();
            }catch (SQLException e) {
                con.rollback();
                logger.error("Failed to delete cards by deck {}", e.getMessage());
                throw new FailedToSoftDeleteCardException(e.getMessage());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToSoftDeleteCardException(e.getMessage());
        }
    }

    @Override
    public List<Card> getCardsForDeck(Deck deck) {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT * FROM card WHERE deck_id = ? AND is_deleted = 0";

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
                    Boolean is_deleted = rs.getBoolean("is_deleted");
                    Card card = new Card(deck, question, answer, tags);
                    card.setId(id);
                    cards.add(card);
                }
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
}
