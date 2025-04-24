package com.cab302.cab302project.model.deck;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.model.SqliteConnection;
import com.cab302.cab302project.model.SqliteCreateTables;
import com.cab302.cab302project.model.user.IUserDAO;
import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.user.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SqliteDeckDAOBookmarkTest {

    private static Connection       con;
    private static IUserDAO         userDAO;
    private static IDeckDAO         deckDAO;
    private static User             user;
    private static Deck             deck;

    @BeforeAll
    static void setupDB() throws Exception {
        SqliteConnection.setTestingModeTrue();
        new SqliteCreateTables();
        con     = SqliteConnection.getInstance();
        userDAO = new SqliteUserDAO();
        deckDAO = new SqliteDeckDAO();

        user = new User("X","Y","x@y","pass");
        userDAO.addUser(user);
        deck = new Deck("BM Test","Bookmark test", user);
        deckDAO.createDeck(deck);
        ApplicationState.login(user);
    }

    @AfterAll
    static void cleanup() throws Exception {
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate("DELETE FROM card");
            stmt.executeUpdate("DELETE FROM deck");
            stmt.executeUpdate("DELETE FROM user");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='deck'");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='user'");
        }
        ApplicationState.logout();
    }

    @Test @Order(1)
    void bookmarkAndRetrieve() {
        // Initially no bookmarks
        List<Deck> empty = deckDAO.getBookmarkedDecks(user);
        assertTrue(empty.isEmpty());

        // Bookmark and verify
        deckDAO.setBookmarked(deck, true);
        List<Deck> one = deckDAO.getBookmarkedDecks(user);
        assertEquals(1, one.size());
        assertEquals(deck.getId(), one.get(0).getId());

        // Unbookmark and verify gone
        deckDAO.setBookmarked(deck, false);
        List<Deck> back = deckDAO.getBookmarkedDecks(user);
        assertTrue(back.isEmpty());
    }
}
