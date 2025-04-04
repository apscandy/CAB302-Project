package com.cab302.cab302project.model.deck;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.model.SqliteConnection;
import com.cab302.cab302project.model.SqliteCreateTables;
import com.cab302.cab302project.model.user.User;
import org.junit.jupiter.api.*;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SqliteDeckDAOTest {

    private static User user;
    private static Connection con;
    private static IDeckDAO deckDAO;
    private static Deck deck;
    private static final String insertTestUser = "INSERT INTO user (first_name, last_name, email, password ) VALUES (?,?,?,?)";


    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        SqliteConnection.setTestingModeTrue();
        user = new User("Andrew", "Clarke", "thegoat@qut.edu.au", "This is a secrete");
        SqliteCreateTables tables = new SqliteCreateTables();
        con = SqliteConnection.getInstance();
        try{
            PreparedStatement statement = con.prepareStatement(insertTestUser);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            user.setId(rs.getInt(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        deckDAO = new SqliteDeckDAO();
        deck =  new Deck("test", "test", user);
        ApplicationState.login(user);
    }

    @AfterEach
    void tearDown() {
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