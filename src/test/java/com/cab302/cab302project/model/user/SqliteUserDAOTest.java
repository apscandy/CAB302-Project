package com.cab302.cab302project.model.user;

import com.cab302.cab302project.model.SqliteConnection;
import org.junit.jupiter.api.*;
import com.cab302.cab302project.model.SqliteCreateTables;
import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SqliteUserDAOTest {
    private static User user;
    private static Connection con;
    private static SqliteUserDAO userDAO;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        SqliteConnection.setTestingModeTrue();
        new SqliteCreateTables();
        con = SqliteConnection.getInstance();
        userDAO = new SqliteUserDAO();
        user = new User("Test", "Still Testing", "AmITestingMyCode@OrIsMyCodeTestingMe.ru", "SameAsEmail");
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
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

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
        user.setPassword(newPassword);
        user.setEmail(newEmail);
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
        User returnUser = userDAO.getUser(user.getEmail());
        assertEquals(user.getId(), returnUser.getId());
        assertEquals(user.getFirstName(), returnUser.getFirstName());
        assertEquals(user.getLastName(), returnUser.getLastName());
        assertEquals(user.getPassword(), returnUser.getPassword());
    }
}
