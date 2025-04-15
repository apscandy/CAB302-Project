package com.cab302.cab302project.model.user;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.model.SqliteConnection;
import org.junit.jupiter.api.*;
import com.cab302.cab302project.model.SqliteCreateTables;
import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SqliteUserDAOTest {
    private static User user;
    private static Connection con;
    private static SqliteUserDAO userDAO;

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        SqliteConnection.setTestingModeTrue();
        new SqliteCreateTables();
        con = SqliteConnection.getInstance();
        userDAO = new SqliteUserDAO();
        user = new User("Test", "Still Testing", "AmITestingMyCode@OrIsMyCodeTestingMe.ru", "SameAsEmail");
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
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
        ApplicationState.logout();
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Test
    @Order(1)
    void testAddUser() {
        userDAO.addUser(user);
        assertEquals(1, user.getId());
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
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

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Test
    @Order(2)
    void testGetUserById() {
        User returnUser = userDAO.getUser(user.getId());
        assertEquals(user.getFirstName(), returnUser.getFirstName());
        assertEquals(user.getLastName(), returnUser.getLastName());
        assertEquals(user.getEmail(), returnUser.getEmail());
        assertEquals(user.getPassword(), returnUser.getPassword());
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
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
