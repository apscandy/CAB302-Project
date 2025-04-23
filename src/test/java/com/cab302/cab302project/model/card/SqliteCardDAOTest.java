// test/java/com/cab302/cab302project/model/card/SqliteCardDAOTest.java
package com.cab302.cab302project.model.card;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.model.SqliteConnection;
import com.cab302.cab302project.model.SqliteCreateTables;
import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.deck.IDeckDAO;
import com.cab302.cab302project.model.deck.SqliteDeckDAO;
import com.cab302.cab302project.model.user.IUserDAO;
import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.user.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SqliteCardDAOTest {

    private static Connection con;
    private static IUserDAO userDAO;
    private static IDeckDAO deckDAO;
    private static ICardDAO cardDAO;
    private static User user;
    private static Deck deck;

    @BeforeAll
    static void initAll() throws Exception {
        // use in-memory test DB
        SqliteConnection.setTestingModeTrue();
        new SqliteCreateTables();
        con      = SqliteConnection.getInstance();
        userDAO  = new SqliteUserDAO();
        deckDAO  = new SqliteDeckDAO();
        cardDAO  = new SqliteCardDAO();

        // create test user & deck
        user = new User("test", "user", "test@example.com", "pwd");
        userDAO.addUser(user);
        deck = new Deck("Test Deck", "for cards", user);
        deckDAO.createDeck(deck);
        ApplicationState.login(user);
    }

    @AfterAll
    static void tearDownAll() throws Exception {
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate("DELETE FROM card");
            stmt.executeUpdate("DELETE FROM deck");
            stmt.executeUpdate("DELETE FROM user");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='card'");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='deck'");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='user'");
        }
        ApplicationState.logout();
    }

    @Test
    @Order(1)
    void testAddAndGetCardsForDeck() {
        Card c1 = new Card(deck, "Q1", "A1", "tag1");
        Card c2 = new Card(deck, "Q2", "A2", "tag2");
        cardDAO.addCard(c1);
        cardDAO.addCard(c2);

        List<Card> cards = cardDAO.getCardsForDeck(deck);
        assertEquals(2, cards.size());
        assertTrue(cards.stream().anyMatch(c -> c.getQuestion().equals("Q1") && c.getAnswer().equals("A1")));
        assertTrue(cards.stream().anyMatch(c -> c.getQuestion().equals("Q2") && c.getAnswer().equals("A2")));
    }

    @Test
    @Order(2)
    void testUpdateCard() {
        List<Card> cards = cardDAO.getCardsForDeck(deck);
        assertFalse(cards.isEmpty());
        Card toUpdate = cards.getFirst();

        toUpdate.setQuestion("Q1 updated");
        toUpdate.setAnswer("A1 updated");
        toUpdate.setTags("newtag");
        cardDAO.updateCard(toUpdate);

        List<Card> updated = cardDAO.getCardsForDeck(deck);
        assertTrue(updated.stream().anyMatch(c ->
                c.getQuestion().equals("Q1 updated") && c.getAnswer().equals("A1 updated") && c.getTags().equals("newtag")
        ));
    }

    @Test
    @Order(3)
    void testSoftDeleteCard() {
        // delete one card
        List<Card> cards = cardDAO.getCardsForDeck(deck);
        int before = cards.size();
        assertTrue(before > 0);
        Card toDelete = cards.get(0);

        cardDAO.softDeleteCard(toDelete);
        List<Card> after = cardDAO.getCardsForDeck(deck);
        assertEquals(before - 1, after.size());
    }

    @Test
    @Order(4)
    void testGetCardAndLoadIntoDeck() {
        // re-add a few cards
        cardDAO.addCard(new Card(deck, "Extra1", "Ans1", ""));
        cardDAO.addCard(new Card(deck, "Extra2", "Ans2", ""));

        // clear deck.cards and reload
        deck.setCards(null);
        cardDAO.getCardAndLoadIntoDeck(deck);

        List<Card> loaded = deck.getCards();
        assertNotNull(loaded);
        assertEquals(cardDAO.getCardsForDeck(deck).size(), loaded.size());
    }
}
