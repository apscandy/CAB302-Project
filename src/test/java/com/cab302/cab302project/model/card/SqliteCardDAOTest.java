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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static Deck testDeck;
    private static User testUser;

    @Test
    @Order(5)
    public void testSmartShufflerWithMockStats() {
        con = SqliteConnection.getInstance();
        deckDAO = new SqliteDeckDAO();
        cardDAO = new SqliteCardDAO();
        userDAO = new SqliteUserDAO();
        testUser = new User("Testtestest", "Testsetet", "aisdhfoaih@oiasdhf.ocm.au", "7gasdfU8:kk");
        userDAO.addUser(testUser);
        testDeck = new Deck("Test Deck", "sakjdfaksdhf", testUser);
        deckDAO.createDeck(testDeck);
        for (int i = 1; i <= 5; i++) {
            Card card = new Card(testDeck, "Q" + i, "A" + i, "");
            cardDAO.addCard(card);
        }
        List<Card> cards = cardDAO.getCardsForDeck(testDeck);
        assertEquals(5, cards.size(), "There should be 5 cards in the deck");

        // Mock review results
        Map<Integer, double[]> cardStats = new HashMap<>();

        for (Card card : cards) {
            switch (card.getQuestion()) {
                case "Q1" -> cardStats.put(card.getId(), new double[]{1, 5}); // 83% wrong
                case "Q2" -> cardStats.put(card.getId(), new double[]{2, 2}); // 50% wrong
                case "Q3" -> cardStats.put(card.getId(), new double[]{0, 3}); // 100% wrong
                case "Q4" -> cardStats.put(card.getId(), new double[]{3, 0}); // 0% wrong
            }
        }

        List<Card> shuffledCards = new SqliteCardDAO().getSmartShuffledCardsForDeck(testDeck, cardStats);
        int indexQ1 = -1, indexQ2 = -1, indexQ3 = -1, indexQ4 = -1, indexQ5 = -1;
        for (int i = 0; i < shuffledCards.size(); i++) {
            Card card = shuffledCards.get(i);
            switch (card.getQuestion()) {
                case "Q1" -> indexQ1 = i;
                case "Q2" -> indexQ2 = i;
                case "Q3" -> indexQ3 = i;
                case "Q4" -> indexQ4 = i;
                case "Q5" -> indexQ5 = i;
            }
        }
        assertTrue( indexQ1 != -1 && indexQ2 != -1 && indexQ3 != -1 && indexQ4 != -1 && indexQ5 != -1, "Not all questions present");
        assertTrue(indexQ1 < indexQ2 && indexQ3 < indexQ2 && indexQ3 < indexQ1, "Wrong order");
    }
}
