package com.cab302.cab302project.model.deck;

import com.cab302.cab302project.model.user.User;

import java.util.List;

public interface IDeckDAO {
    public void createDeck(Deck deck);
    public void updateDeck(Deck deck);
    public void deleteDeck(Deck deck);
    public List<Deck> getDecks(User user);
    public Deck getDeck(int id);
}
