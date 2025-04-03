package com.flashcards.cab.model.deck;

import com.flashcards.cab.model.user.User;

import java.util.List;

public interface IDeckDAO {
    public void createDeck(Deck deck);
    public void updateDeck(Deck deck);
    public void deleteDeck(Deck deck);
    public List<Deck> getDecks(User user);
    public Deck getDeck(int id);
}
