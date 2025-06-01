package com.cab302.cab302project.model.session;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.TestModes;
import com.cab302.cab302project.model.SqliteConnection;
import com.cab302.cab302project.model.SqliteCreateTables;
import com.cab302.cab302project.model.card.Card;
import com.cab302.cab302project.model.card.ICardDAO;
import com.cab302.cab302project.model.card.SqliteCardDAO;
import com.cab302.cab302project.model.deck.Deck;
import com.cab302.cab302project.model.deck.IDeckDAO;
import com.cab302.cab302project.model.deck.SqliteDeckDAO;
import com.cab302.cab302project.model.user.IUserDAO;
import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.user.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SqliteSessionDAOTest {

    private static User user;
    private static IUserDAO userDAO;
    private static Deck deck;
    private static IDeckDAO deckDAO;
    private static Session session;
    private static ISessionDAO sessionDAO;
    private static SessionResults sessionResults;
    private static Card card1;
    private static Card card2;
    private static ICardDAO cardDAO;
    private static Connection connection;


    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        SqliteConnection.setTestingModeTrue();
        new SqliteCreateTables();
        connection = SqliteConnection.getInstance();
        user = new User("Andy", "Clarke", "test@test.test", "Super strong password");
        userDAO = new SqliteUserDAO();
        userDAO.addUser(user);

        deck = new Deck("Chem 101", "Flash cards for Chem 101 exam ", user);
        deckDAO = new SqliteDeckDAO();
        deckDAO.createDeck(deck);

        card1 = new Card(deck, "What is Avogadro’s Number, and why is it important in chemistry?", "6.022 x 10²³ entities per mole (usually atoms, molecules, or ions).", "");
        card2 = new Card(deck , "Define the term \"pH\" and explain what it indicates about a solution’s acidity or basicity.", " A measure of the hydrogen ion (H⁺) concentration in a solution. It’s defined as the negative logarithm (base 10) of the hydrogen ion concentration: pH = -log[H⁺]", "");
        cardDAO = new SqliteCardDAO();
        cardDAO.addCard(card1);
        cardDAO.addCard(card2);

        session = new Session(deck , user);
        sessionDAO = new SqliteSessionDAO();

        ApplicationState.setCurrentModeSequential();
        ApplicationState.login(user);
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        try{
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM user");
            stmt.executeUpdate("DELETE FROM deck");
            stmt.executeUpdate("DELETE FROM card");
            stmt.executeUpdate("DELETE FROM session");
            stmt.executeUpdate("DELETE FROM session_results");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='user'");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='session'");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='session_results'");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='deck'");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='card'");
            stmt.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }finally {
            ApplicationState.logout();
            ApplicationState.clearCurrentMode();
        }
    }

    @Test
    @Order(1)
    void createSession() {
        sessionDAO.createSession(session);
        assertEquals(1, session.getId());
    }

    @Test
    @Order(5)
    void endSession() {
        sessionDAO.endSession(session);
        assertEquals(1, session.getId());
        assertEquals(true, session.getSessionFinished());
    }

    @Test
    @Order(2)
    void createSessionResult() {
        SessionResults sessionResult = new SessionResults(session, card1);
        sessionDAO.createSessionResult(sessionResult);
        assertEquals(1, sessionResult.getId());
        assertEquals(1, sessionResult.getCardId());
    }

    @Test
    @Order(3)
    void sessionResultCardCorrect() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusSeconds(30);
        SessionResults sessionResult = new SessionResults(session, card1);
        sessionDAO.sessionResultCardCorrect(sessionResult, startTime, endTime);
        assertEquals(30, Integer.parseInt(sessionResult.getTimeToAnswer()));
        assertEquals(true, sessionResult.getCorrect());
        assertEquals(false, sessionResult.getIncorrect());

    }

    @Test
    @Order(4)
    void sessionResultCardIncorrect() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusSeconds(30);
        SessionResults sessionResult = new SessionResults(session, card2);
        sessionDAO.sessionResultCardIncorrect(sessionResult, startTime, endTime);
        assertEquals(30, Integer.parseInt(sessionResult.getTimeToAnswer()));
        assertEquals(false, sessionResult.getCorrect());
        assertEquals(true, sessionResult.getIncorrect());
    }
}