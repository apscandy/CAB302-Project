package com.cab302.cab302project.controller;

import com.cab302.cab302project.controller.testMode.CardShuffler;
import com.cab302.cab302project.model.SqliteConnection;
import com.cab302.cab302project.model.SqliteCreateTables;
import com.cab302.cab302project.model.card.Card;
import com.cab302.cab302project.model.card.SqliteCardDAO;
import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.deck.SqliteDeckDAO;
import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.user.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing for smart-card shuffler
 * Expected: Returns a smart-shuffled list of cards:
 * - Cards with wrong rate >= 0.55 are first, sorted from highest to lowest wrong rate
 * - Remaining cards are shown in random order
 *
 * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
 */
public class CardShufflerTest {

        private static Connection con;
        private static SqliteDeckDAO deckDAO;
        private static SqliteCardDAO cardDAO;
        private static SqliteUserDAO userDAO;

        private static Deck testDeck;
        private static User testUser;

        @BeforeAll
        static void setUpBeforeClass() {
            SqliteConnection.setTestingModeTrue();
            new SqliteCreateTables();
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
        }

        @AfterAll
        static void tearDown() {
            try {
                try (Statement stmt = con.createStatement()) {
                    stmt.executeUpdate("DELETE FROM card");
                    stmt.executeUpdate("DELETE FROM deck");
                    stmt.executeUpdate("DELETE FROM user");
                    stmt.executeUpdate("delete from sqlite_sequence where name='user'");
                    stmt.executeUpdate("delete from sqlite_sequence where name='deck'");
                    stmt.executeUpdate("delete from sqlite_sequence where name='card'");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Test
        public void testSmartShufflerWithMockStats() {
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

            List<Card> shuffledCards = new CardShuffler().getSmartShuffledCardsForDeck(testDeck, cardStats);
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

