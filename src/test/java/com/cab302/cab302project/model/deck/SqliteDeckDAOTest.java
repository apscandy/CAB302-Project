package com.cab302.cab302project.model.deck;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.model.SqliteConnection;
import com.cab302.cab302project.model.SqliteCreateTables;
import com.cab302.cab302project.model.user.IUserDAO;
import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.user.User;
import org.junit.jupiter.api.*;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SqliteDeckDAOTest {

    private static User user;
    private static Connection con;
    private static IDeckDAO deckDAO;
    private static IUserDAO userDAO;
    private static Deck deck;


    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        SqliteConnection.setTestingModeTrue();
        new SqliteCreateTables();
        con = SqliteConnection.getInstance();
        deckDAO = new SqliteDeckDAO();
        userDAO = new SqliteUserDAO();
        user = new User("test-user-fName", "test-user-lName", "test-user-email", "test-password");
        userDAO.addUser(user);
        deck =  new Deck("test", "test", user);
        ApplicationState.login(user);
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        try{
            Statement stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM user");
            stmt.executeUpdate("DELETE FROM user_security_question");
            stmt.executeUpdate("DELETE FROM deck");
            stmt.executeUpdate("DELETE FROM card");
            stmt.executeUpdate("delete from sqlite_sequence where name='user'");
            stmt.executeUpdate("delete from sqlite_sequence where name='user_security_question'");
            stmt.executeUpdate("delete from sqlite_sequence where name='deck'");
            stmt.executeUpdate("delete from sqlite_sequence where name='card'");
            stmt.close();
            ApplicationState.logout();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        ApplicationState.logout();
    }

    @Test
    @Order(1)
    void createDeck() {
        deckDAO.createDeck(deck);
        assertNotEquals(0, deck.getId());
    }

    @Test
    @Order(5)
    void updateDeck() {
        deck.setName("ant man");

        deckDAO.updateDeck(deck);
        Deck updatedDeck = deckDAO.getDeck(deck.getId());
        assertEquals(deck.getName(), updatedDeck.getName());
    }

    @Test
    @Order(4)
    void deleteDeck() {
        Deck d = deckDAO.getDeck(4);
        deckDAO.deleteDeck(d);
        List<Deck> decks = deckDAO.getDecks(user);
        assertEquals(3, decks.size());
    }

    @Test
    @Order(3)
    void getDecks() {
        Deck deckOne = new Deck("test1", "test1", user);
        Deck deckTwo = new Deck("test2", "test2", user);
        Deck deckThree = new Deck("test3", "test3", user);
        deckDAO.createDeck(deckOne);
        deckDAO.createDeck(deckTwo);
        deckDAO.createDeck(deckThree);

        List<Deck> decks = deckDAO.getDecks(user);

        assertEquals(4, decks.size());
    }

    @Test
    @Order(2)
    void getDeck() {
        Deck d = deckDAO.getDeck(1);
        assertNotNull(d);
        assertEquals(deck.getId(), d.getId());
    }

    @Test
    @Order(6)
    void softDeleteDeck_ShouldAppearInNormalListBeforeDeletion() {
        Deck deckToSoftDelete = new Deck("soft-delete-test", "test description", user);
        deckDAO.createDeck(deckToSoftDelete);

        List<Deck> decksBeforeSoftDelete = deckDAO.getDecks(user);
        boolean deckFoundBeforeDelete = decksBeforeSoftDelete.stream()
                .anyMatch(d -> d.getId() == deckToSoftDelete.getId());
        assertTrue(deckFoundBeforeDelete);
    }

    @Test
    @Order(7)
    void softDeleteDeck_ShouldRemoveFromNormalList() {
        Deck deckToSoftDelete = new Deck("soft-delete-test-2", "test description", user);
        deckDAO.createDeck(deckToSoftDelete);

        deckDAO.softDeleteDeck(deckToSoftDelete);

        List<Deck> decksAfterSoftDelete = deckDAO.getDecks(user);
        boolean deckFoundAfterDelete = decksAfterSoftDelete.stream()
                .anyMatch(d -> d.getId() == deckToSoftDelete.getId());
        assertFalse(deckFoundAfterDelete);
    }

    @Test
    @Order(8)
    void softDeleteDeck_ShouldAppearInSoftDeletedList() {
        Deck deckToSoftDelete = new Deck("soft-delete-test-3", "test description", user);
        deckDAO.createDeck(deckToSoftDelete);
        deckDAO.softDeleteDeck(deckToSoftDelete);

        List<Deck> softDeletedDecks = deckDAO.getSoftDeletedDecks(user);
        boolean deckFoundInSoftDeleted = softDeletedDecks.stream()
                .anyMatch(d -> d.getId() == deckToSoftDelete.getId());
        assertTrue(deckFoundInSoftDeleted);
    }

    @Test
    @Order(9)
    void restoreDeck_ShouldRemoveFromSoftDeletedList() {
        Deck deckToRestore = new Deck("restore-test", "test description", user);
        deckDAO.createDeck(deckToRestore);
        deckDAO.softDeleteDeck(deckToRestore);

        deckDAO.restoreDeck(deckToRestore);

        List<Deck> softDeletedDecksAfterRestore = deckDAO.getSoftDeletedDecks(user);
        boolean stillFoundInSoftDeleted = softDeletedDecksAfterRestore.stream()
                .anyMatch(d -> d.getId() == deckToRestore.getId());
        assertFalse(stillFoundInSoftDeleted);
    }

    @Test
    @Order(10)
    void restoreDeck_ShouldAppearInNormalListAgain() {
        Deck deckToRestore = new Deck("restore-test-2", "test description", user);
        deckDAO.createDeck(deckToRestore);
        deckDAO.softDeleteDeck(deckToRestore);

        deckDAO.restoreDeck(deckToRestore);

        List<Deck> normalDecksAfterRestore = deckDAO.getDecks(user);
        boolean foundInNormalDecks = normalDecksAfterRestore.stream()
                .anyMatch(d -> d.getId() == deckToRestore.getId());
        assertTrue(foundInNormalDecks);
    }

    @Test
    @Order(11)
    void getSoftDeletedDecks_ShouldContainSoftDeletedDecks() {
        Deck deckToSoftDelete1 = new Deck("soft-delete-test-1", "test description 1", user);

        deckDAO.createDeck(deckToSoftDelete1);
        deckDAO.softDeleteDeck(deckToSoftDelete1);

        List<Deck> softDeletedDecks = deckDAO.getSoftDeletedDecks(user);

        boolean deck1Found = softDeletedDecks.stream()
                .anyMatch(d -> d.getId() == deckToSoftDelete1.getId());

        assertTrue(deck1Found);
    }

    @Test
    @Order(12)
    void getSoftDeletedDecks_ShouldNotContainNormalDecks() {
        Deck normalDeck = new Deck("normal-deck", "test description normal", user);
        deckDAO.createDeck(normalDeck);

        List<Deck> softDeletedDecks = deckDAO.getSoftDeletedDecks(user);

        boolean normalDeckFound = softDeletedDecks.stream()
                .anyMatch(d -> d.getId() == normalDeck.getId());
        assertFalse(normalDeckFound);
    }

    @Test
    @Order(13)
    void getSoftDeletedDecks_NormalDeckShouldStayInRegularList() {
        Deck normalDeck = new Deck("normal-deck-2", "test description normal", user);
        deckDAO.createDeck(normalDeck);

        List<Deck> normalDecks = deckDAO.getDecks(user);
        boolean normalDeckInRegularList = normalDecks.stream()
                .anyMatch(d -> d.getId() == normalDeck.getId());
        assertTrue(normalDeckInRegularList);
    }
}