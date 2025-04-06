package com.cab302.cab302project.model.card;

import com.cab302.cab302project.model.SqliteConnection;
import com.cab302.cab302project.model.deck.Deck;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteCardDAO implements ICardDAO {

    private final Connection con;

    public SqliteCardDAO() {
        this.con = SqliteConnection.getInstance();
        createCardTable();
    }

    private void createCardTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS card (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                deck_id INTEGER NOT NULL,
                question TEXT NOT NULL,
                answer TEXT NOT NULL,
                tags TEXT,
                is_deleted BOOLEAN DEFAULT 0,
                FOREIGN KEY (deck_id) REFERENCES deck(id) ON DELETE CASCADE
            );
        """;

        try (Statement stmt = con.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addCard(Card card) {
        String sql = "INSERT INTO card (deck_id, question, answer, tags, is_deleted) VALUES (?, ?, ?, ?, 0)";
        try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, card.getDeck().getId());
            stmt.setString(2, card.getQuestion());
            stmt.setString(3, card.getAnswer());
            stmt.setString(4, card.getTags());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                card.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCard(Card card) {
        String sql = "UPDATE card SET question = ?, answer = ?, tags = ? WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, card.getQuestion());
            stmt.setString(2, card.getAnswer());
            stmt.setString(3, card.getTags());
            stmt.setInt(4, card.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void softDeleteCard(Card card) {
        String sql = "UPDATE card SET is_deleted = 1 WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, card.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void softDeleteCardsByDeck(int deckId) {
        String sql = "UPDATE card SET is_deleted = 1 WHERE deck_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, deckId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Card> getCardsForDeck(Deck deck) {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT * FROM card WHERE deck_id = ? AND is_deleted = 0";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, deck.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Card card = new Card(
                        deck,
                        rs.getString("question"),
                        rs.getString("answer"),
                        rs.getString("tags")
                );
                card.setId(rs.getInt("id"));
                card.setDeleted(rs.getBoolean("is_deleted"));
                cards.add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }
}
