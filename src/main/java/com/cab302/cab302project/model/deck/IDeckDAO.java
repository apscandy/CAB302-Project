package com.cab302.cab302project.model.deck;

import com.cab302.cab302project.model.user.User;

import java.util.List;


/**
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public interface IDeckDAO {
    public void createDeck(Deck deck);
    public void updateDeck(Deck deck);
    public void deleteDeck(Deck deck);
    public void softDeleteDeck(Deck deck);
    public void restoreDeck(Deck deck);
    public List<Deck> getDecks(User user);
    public List<Deck> getSoftDeletedDecks(User user);
    public Deck getDeck(int id);
}
