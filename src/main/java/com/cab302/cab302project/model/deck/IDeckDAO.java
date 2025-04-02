package com.cab302.cab302project.model.deck;

import com.cab302.cab302project.model.user.User;

import java.util.List;

public interface IDeckDAO {
    public void CreateDeck(Deck deck);
    public void UpdateDeck(Deck deck);
    public void DeleteDeck(Deck deck);
    public List<Deck> GetDecks(User user);
    public Deck GetDeck(int id);
}
