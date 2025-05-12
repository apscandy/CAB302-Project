package com.cab302.cab302project.model.deck;

import com.cab302.cab302project.model.user.User;

import java.util.List;

/**
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public interface IDeckDAO {
    void createDeck(Deck deck);
    void updateDeck(Deck deck);
    void deleteDeck(Deck deck);
    void softDeleteDeck(Deck deck);
    void restoreDeck(Deck deck);
    List<Deck> getDecks(User user);
    List<Deck> getSoftDeletedDecks(User user);
    Deck getDeck(int id);
    void setBookmarked(Deck deck, boolean bookmarked);
    List<Deck> getBookmarkedDecks(User user);
}
