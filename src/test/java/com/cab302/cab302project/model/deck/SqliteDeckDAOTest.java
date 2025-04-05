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
    private static final String insertTestUser = "INSERT INTO user (first_name, last_name, email, password ) VALUES (?,?,?,?)";


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
            String sql = "DELETE FROM user";
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
            sql = "DELETE FROM deck";
            stmt.executeUpdate(sql);
            stmt.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
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
}