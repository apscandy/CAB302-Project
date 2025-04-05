package com.cab302.cab302project.model.user;

import com.cab302.cab302project.model.SqliteConnection;
import com.cab302.cab302project.model.deck.IDeckDAO;
import org.junit.jupiter.api.*;
import com.cab302.cab302project.model.SqliteCreateTables;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SqliteUserDAOTest {
    private static User user;
    private static Connection con;
    private static SqliteUserDAO userDAO;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        SqliteConnection.setTestingModeTrue();
        SqliteCreateTables tables = new SqliteCreateTables();
        con = SqliteConnection.getInstance();
        userDAO = new SqliteUserDAO();
        user = new User("Test", "Still Testing", "AmITestingMyCode@OrIsMyCodeTestingMe.ru", "SameAsEmail");
    }

    @AfterEach
    void tearDown() {}

    @Test
    @Order(1)
    void testAddUser() {
        userDAO.addUser(user);
        assertEquals(1, user.getId());
    }

    @Test
    @Order(3)
    void testUpdateUser() {
        String newPassword = "AntiMFA";
        String newEmail = "test@test.com";
        userDAO.updateUser(user);
        User returnUser = userDAO.getUser(user.getId());
        assertEquals(newPassword, returnUser.getPassword());
        assertEquals(newEmail, returnUser.getEmail());
    }

    @Test
    @Order(2)
    void testGetUserById() {
        User returnUser = userDAO.getUser(user.getId());
        assertEquals(user.getFirstName(), returnUser.getFirstName());
        assertEquals(user.getLastName(), returnUser.getLastName());
        assertEquals(user.getEmail(), returnUser.getEmail());
        assertEquals(user.getPassword(), returnUser.getPassword());
    }

    @Test
    @Order(4)
    void testGetUserByEmail() {
        User returnUser = userDAO.getUser("test@test.com");
        assertEquals(user.getId(), returnUser.getId());
        assertEquals(user.getFirstName(), returnUser.getFirstName());
        assertEquals(user.getLastName(), returnUser.getLastName());
        assertEquals("AntiMFA", returnUser.getPassword());
    }
}
