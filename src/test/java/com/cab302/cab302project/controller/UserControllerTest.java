package com.cab302.cab302project.controller;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.controller.user.UserController;
import com.cab302.cab302project.model.SqliteConnection;
import com.cab302.cab302project.model.SqliteCreateTables;
import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.user.User;
import com.cab302.cab302project.model.userSecQuestions.SqliteUserSecurityQuestionDAO;
import com.cab302.cab302project.model.userSecQuestions.UserSecurityQuestion;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.Statement;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    private static Connection con;
    private static SqliteUserDAO userDAO;
    private static SqliteUserSecurityQuestionDAO questionDAO;
    private static User testUser;
    private static UserSecurityQuestion testQuestions;
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        SqliteConnection.setTestingModeTrue();
        new SqliteCreateTables();
        con = SqliteConnection.getInstance();
        userDAO = new SqliteUserDAO();
        testUser = new User (
                "Testing", "Still Testing", "myCodeIsTestingMe@malicious.ru", "MyDogBirthday"
        );
        questionDAO = new SqliteUserSecurityQuestionDAO();
        testQuestions = new UserSecurityQuestion(testUser);
        testQuestions.setQuestionOne("What is credit card expiry date?");
        testQuestions.setQuestionTwo("What is your credit card number?");
        testQuestions.setQuestionThree("What is your credit card pin?");
        testQuestions.setAnswerOne("Tomorrow");
        testQuestions.setAnswerTwo("1234 5678 9101 1121");
        testQuestions.setAnswerThree("MyDogBirthday");
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM user");
            stmt.executeUpdate("DELETE FROM user_security_question");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='user'");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='user_security_question'");
            stmt.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(1)
    void testRegisterUser() {
        boolean result = UserController.register(testUser, userDAO);
        assertTrue(result);
        User dbUserCheck = userDAO.getUser(testUser.getEmail());
        assertNotNull(dbUserCheck);
        assertEquals(testUser.getEmail(), dbUserCheck.getEmail());
    }

    @Test
    @Order(2)
    void testAuthenticateUser() {
        boolean success = UserController.authenticate (
                testUser.getEmail(), "MyDogBirthday", userDAO
        );
        assertTrue(success);
        assertTrue(ApplicationState.isUserLoggedIn());
        assertEquals(testUser.getEmail(), ApplicationState.getCurrentUser().getEmail());
        ApplicationState.logout();
        boolean fail = UserController.authenticate (
                testUser.getEmail(), "WrongPassword", userDAO
        );
        assertFalse(fail);
        assertFalse(ApplicationState.isUserLoggedIn());
        assertNull(ApplicationState.getCurrentUser());
    }

    @Test
    @Order(3)
    void testEmailCheck() {
        boolean checkExistEmail = UserController.emailCheck(testUser.getEmail(), userDAO);
        assertTrue(checkExistEmail);
        boolean checkNonExistEmail = UserController.emailCheck("hacker@test.ru", userDAO);
        assertFalse(checkNonExistEmail);
    }

    @Test
    @Order(4)
    void testResetPassword() {
        String newPassword = "newPassword";
        boolean successReset = UserController.resetPassword (
            testUser.getEmail(), newPassword, userDAO
        );
        assertTrue(successReset);
        boolean successAuth = UserController.authenticate (
                testUser.getEmail(), newPassword, userDAO
        );
        assertTrue(successAuth);
    }


}
