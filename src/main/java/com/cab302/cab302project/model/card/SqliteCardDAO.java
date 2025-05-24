package com.cab302.cab302project.model.card;

import com.cab302.cab302project.error.model.card.*;
import com.cab302.cab302project.error.model.deck.DeckIsNullException;
import com.cab302.cab302project.error.model.deck.FailedToDeleteDeckException;
import com.cab302.cab302project.model.SqliteConnection;
import com.cab302.cab302project.model.deck.Deck;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;

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
    private final String deleteCardSQL = "DELETE FROM card WHERE id = ? AND is_deleted = 1";
    private final String softDeleteSQL = "UPDATE card SET is_deleted = ? WHERE id = ?";
    private final String getCardsForDeckSQL = "SELECT * FROM card WHERE deck_id = ? AND is_deleted = ?";
    private final String getRandomCardsSQL = "SELECT * FROM card WHERE deck_id = ? AND is_deleted = 0 ORDER BY RANDOM()";


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
                ResultSet rs = insertStatement.getGeneratedKeys();
                if (rs.next()) {
                    card.setId(rs.getInt(1));
                }
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
                updateStatement.setString(1, card.getQuestion());
                updateStatement.setString(2, card.getAnswer());
                updateStatement.setString(3, card.getTags());
                updateStatement.setInt(4, card.getId());
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

    @Override
    public void deleteCard(Card card) {
        if (card == null) {
            throw new CardIsNullException("Card cannot be null");
        }
        try {
            // Transaction try/catch block
            con.setAutoCommit(false);
            try (PreparedStatement deleteStatement = con.prepareStatement(deleteCardSQL)) {
                deleteStatement.setInt(1, card.getId());
                deleteStatement.executeUpdate();
                con.commit();
                deleteStatement.close();
            }catch (SQLException  e) {
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToDeleteCardsException(e.getMessage());
            }finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToDeleteCardsException(e.getMessage());
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
                softDeleteStatement.setBoolean(1, true);
                softDeleteStatement.setInt(2, card.getId());
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

    @Override
    public void restoreCard(Card card) {
        try {
            con.setAutoCommit(false);
            try (PreparedStatement softDeleteStatement = con.prepareStatement(softDeleteSQL)) {
                softDeleteStatement.setBoolean(1, false);
                softDeleteStatement.setInt(2, card.getId());
                softDeleteStatement.executeUpdate();
                con.commit();
                softDeleteStatement.close();
            }catch (SQLException e) {
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToRestoreCardException(e.getMessage());
            }finally {
                con.setAutoCommit(true);
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToRestoreCardException(e.getMessage());
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
            try (PreparedStatement getCardStatement = con.prepareStatement(getCardsForDeckSQL)) {
                getCardStatement.setInt(1, deck.getId());
                getCardStatement.setBoolean(2, false);
                ResultSet rs = getCardStatement.executeQuery();
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
                getCardStatement.close();
                rs.close();
                deck.setCards(cards);
            } catch (SQLException e) {
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToGetCardsException(e.getMessage());
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToGetCardsException(e.getMessage());
        }
        return cards;
    }


    /**
     * Loads cards into the given deck by querying the database
     * and setting the deck’s internal list.
     *
     * @param deck Deck to load cards into
     */
    @Override
    public void getCardAndLoadIntoDeck(Deck deck) {
        List<Card> cards = new ArrayList<>();
        try {
            con.setAutoCommit(false);
            try (PreparedStatement getCardStatement = con.prepareStatement(getCardsForDeckSQL)) {
                getCardStatement.setInt(1, deck.getId());
                getCardStatement.setBoolean(2, false);
                ResultSet rs = getCardStatement.executeQuery();
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
                getCardStatement.close();
                rs.close();
            } catch (SQLException e) {
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToGetCardsException(e.getMessage());
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToGetCardsException(e.getMessage());
        }
    }

    @Override
    public void restoreCardsByDeck(Deck deck) {
        if (deck == null || deck.getUserId() == 0 || deck.getId() == 0){
            throw new DeckIsNullException("Deck cannot be null");
        }
        try {
            con.setAutoCommit(false);
            try(PreparedStatement softDelete = con.prepareStatement(softDeleteSQL)){
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

    @Override
    public List<Card> getSoftDeletedCardsForDeck(Deck deck) {
        List<Card> cards = new ArrayList<>();
        try {
            con.setAutoCommit(false);
            try (PreparedStatement getCardStatment = con.prepareStatement(getCardsForDeckSQL)) {
                getCardStatment.setInt(1, deck.getId());
                getCardStatment.setInt(2, 1);
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
     * Retrieves a randomized list of {@link Card} objects associated with the given {@link Deck}.
     *
     * <p>
     * This method queries the database for cards belonging to the specified deck and returns them
     * in randomized order. It uses a prepared SQL statement defined by {@code getRandomCardsSQL}.
     * The method runs inside a database transaction to ensure consistency, and rolls back in case
     * of any error.
     * </p>
     *
     * @param deck The {@link Deck} for which to retrieve randomized cards. Must not be {@code null}
     *             and must have a valid (non-zero) ID.
     * @return A list of {@link Card} objects belonging to the deck, in randomized order.
     * @throws DeckIsNullException If the provided deck is {@code null} or has an invalid ID.
     * @throws FailedToGetCardsException If a SQL error occurs during card retrieval or transaction handling.
     * @author Dang Linh Phan - lewis (n11781840)
     */
    @Override
    public List<Card> getRandomizedCardsForDeck(Deck deck) {
        if (deck == null || deck.getId() == 0) {
            throw new DeckIsNullException("Invalid deck");
        }

        List<Card> cards = new ArrayList<>();
        String sql = getRandomCardsSQL;

        try {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, deck.getId());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Card c = new Card(
                                deck,
                                rs.getString("question"),
                                rs.getString("answer"),
                                rs.getString("tags")
                        );
                        c.setId(rs.getInt("id"));
                        cards.add(c);
                    }
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                logger.error("getRandomizedCardsForDeck failed", e);
                throw new FailedToGetCardsException(e.getMessage(), e);
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            logger.error("Transaction error", e);
            throw new FailedToGetCardsException(e.getMessage(), e);
        }

        return cards;
    }

    /**
     * Returns a smart-shuffled list of cards:
     * - Cards with wrong rate >= 0.55 are first, sorted from highest to lowest wrong rate
     * - Remaining cards are shown in random order
     *
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    public List<Card> getSmartShuffledCardsForDeck(Deck deck, Map<Integer, double[]> cardResults) {
        List<Card> allCards = deck.getCards();
        List<Card> wrongCards = new ArrayList<>();
        Map<Integer, Double> wrongRates = new HashMap<>();
        List<Card> okCards = new ArrayList<>();
        for (Card card : allCards) {
            double[] stats = cardResults.get(card.getId());
            if (stats == null) {
                okCards.add(card);
                continue;
            }
            double correct = stats[0];
            double incorrect = stats[1];
            double total = correct + incorrect;
            if (total == 0) {
                okCards.add(card);
            } else {
                double wrongRate = incorrect / total;
                if (wrongRate >= 0.55) {
                    wrongCards.add(card);
                    wrongRates.put(card.getId(), wrongRate);
                } else {
                    okCards.add(card);
                }
            }
        }

        // Cards with wrong rate > 55% are sorted by wrong rate in desc order
        wrongCards.sort((c1, c2) -> Double.compare(
                wrongRates.getOrDefault(c2.getId(), 0.0),
                wrongRates.getOrDefault(c1.getId(), 0.0)
        ));

        // The rest will be shuffled in a random order
        Collections.shuffle(okCards);

        List<Card> result = new ArrayList<>();
        result.addAll(wrongCards);
        result.addAll(okCards);
        return result;
    }
}
