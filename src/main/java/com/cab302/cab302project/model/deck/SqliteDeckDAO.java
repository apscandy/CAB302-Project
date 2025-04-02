package com.cab302.cab302project.model.deck;

import com.cab302.cab302project.model.SqliteConnection;
import com.cab302.cab302project.model.user.User;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class SqliteDeckDAO implements IDeckDAO {

    private final Connection con;

    public SqliteDeckDAO() {
        con = SqliteConnection.getInstance();
    }

    @Override
    public void CreateDeck(Deck deck) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void UpdateDeck(Deck deck) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void DeleteDeck(Deck deck) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Deck> GetDecks(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
//        return List.of();
    }

    @Override
    public Deck GetDeck(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
//        return null;
    }
}
