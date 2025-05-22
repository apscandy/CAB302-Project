package com.cab302.cab302project.model.deck;

import com.cab302.cab302project.model.user.User;

import java.util.List;
import java.util.Map;

/**
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public interface IDeckDAO {
    List<Deck> getDecks(User user);
    Deck getDeck(int id);
    void createDeck(Deck deck);
    void updateDeck(Deck deck);
    void deleteDeck(Deck deck);

    void softDeleteDeck(Deck deck);
    List<Deck> getSoftDeletedDecks(User user);
    void restoreDeck(Deck deck);

    void setBookmarked(Deck deck, boolean bookmarked);
    List<Deck> getBookmarkedDecks(User user);
}
