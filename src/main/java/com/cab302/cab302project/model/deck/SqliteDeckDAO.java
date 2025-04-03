package com.cab302.cab302project.model.deck;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.model.SqliteConnection;
import com.cab302.cab302project.model.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqliteDeckDAO implements IDeckDAO {

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
        try {
            PreparedStatement insertStatement = con.prepareStatement(createDeckSQL);
            insertStatement.setInt(1, deck.getUserId());
            insertStatement.setString(2, deck.getName());
            insertStatement.setString(3, deck.getDescription());
            insertStatement.executeUpdate();
            ResultSet resultSet = insertStatement.getGeneratedKeys();
            if (resultSet.next()) {
                deck.setId(resultSet.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateDeck(Deck deck) {
        try {
            PreparedStatement insertStatement = con.prepareStatement(updateDeckSQL);
            insertStatement.setString(1, deck.getName());
            insertStatement.setString(2, deck.getDescription());
            insertStatement.setInt(3, deck.getId());
            insertStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteDeck(Deck deck) {
       try{
           PreparedStatement deleteStatement = con.prepareStatement(deleteDeckSQL);
           deleteStatement.setInt(1, deck.getId());
           deleteStatement.executeUpdate();
       } catch (Exception e) {
           e.printStackTrace();
       }
    }

    /*
    TODO: look are replacing user with ApplicationState.getCurrentUser()
     */
    @Override
    public List<Deck> getDecks(User user) {
        List<Deck> decks = new ArrayList<Deck>();
        try{
            PreparedStatement selectStatement = con.prepareStatement(selectDeckSQL);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decks;
    }

    @Override
    public Deck getDeck(int id) {
        Deck deck = null;
        try{
            PreparedStatement selectStatement = con.prepareStatement(selectDeckByIdSQL);
            selectStatement.setInt(1, id);
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                int deckId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                deck = new Deck(name, description, ApplicationState.getCurrentUser());
                deck.setId(deckId);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return deck;
    }
}
