package com.cab302.cab302project.controller;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.controller.user.Authentication;
import com.cab302.cab302project.error.UserAlreadyLoggedInException;
import com.cab302.cab302project.error.authenicaton.*;
import com.cab302.cab302project.model.SqliteConnection;
import com.cab302.cab302project.model.SqliteCreateTables;
import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.user.User;
import com.cab302.cab302project.model.userSecQuestions.SqliteUserSecurityQuestionDAO;
import com.cab302.cab302project.model.userSecQuestions.UserSecurityQuestion;
import com.cab302.cab302project.util.PasswordUtils;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.Statement;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthenticationTest {

    private static Connection con;
    private static SqliteUserDAO userDAO;
    private static SqliteUserSecurityQuestionDAO questionDAO;
    private static User testUser;
    private static UserSecurityQuestion testQuestions;
    private static Authentication authentication;

    @BeforeAll
    static void setUpBeforeClass() {
        SqliteConnection.setTestingModeTrue();
        new SqliteCreateTables();
        con = SqliteConnection.getInstance();
        userDAO = new SqliteUserDAO();
        questionDAO = new SqliteUserSecurityQuestionDAO();
        testUser = new User ("Testing", "Still Testing", "johnwick@malicious.com", "MyDogBirthday093j-kd!");
        testQuestions = new UserSecurityQuestion(testUser);
        testQuestions.setQuestionOne("What is credit card expiry date?");
        testQuestions.setQuestionTwo("What is your credit card number?");
        testQuestions.setQuestionThree("What is your credit card pin?");
        testQuestions.setAnswerOne("Tomorrow");
        testQuestions.setAnswerTwo("1234 5678 9101 1121");
        testQuestions.setAnswerThree("MyDogBirthday");
        authentication = new Authentication();
    }

    @AfterAll
    static void tearDownAfterClass() {
        try {
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
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    @Order(1)
    void testRegisterUser() {

        testUser.setPassword("");
        assertThrows(PasswordEmptyException.class, ()-> authentication.register(testUser));

        testUser.setEmail("");
        assertThrows(EmailEmptyException.class, () -> authentication.register(testUser));

        testUser.setEmail("johnwick");
        testUser.setPassword("MyDogBirthday093j-kd!");
        assertThrows(InvalidEmailFormatException.class, () -> authentication.register(testUser));

        testUser.setEmail("johnwick@malicious.com.ru");
        testUser.setPassword("VerySecure");
        assertThrows(InvalidPasswordFormatException.class, () -> authentication.register(testUser));

        testUser.setPassword("MyDogBirthday093j-kd!");
        testUser.setEmail("johnwick@malicious.com");
        boolean result = authentication.register(testUser);
        assertTrue(result);
        assertThrows(EmailAlreadyInUseException.class, () -> authentication.register(testUser));
    }

    @Test
    @Order(2)
    void testAuthenticateUser() {
        assertFalse(ApplicationState.isUserLoggedIn());
        assertThrows(PasswordEmptyException.class, () -> authentication.authenticate(testUser.getEmail(), ""));
        assertThrows(EmailEmptyException.class, () -> authentication.authenticate("", testUser.getPassword()));
        assertThrows(UserNotFoundException.class, () -> authentication.authenticate("jimbob@emails.com", testUser.getPassword()));
        assertThrows(InvalidEmailFormatException.class, () -> authentication.authenticate("johnwick", testUser.getPassword()));
        assertThrows(PasswordComparisonException.class, () -> authentication.authenticate(testUser.getEmail(), "MyDogBirthday093j-kd!asdfoiajs"));
        assertDoesNotThrow(()-> authentication.authenticate(testUser.getEmail(), "MyDogBirthday093j-kd!"));
        assertTrue(ApplicationState.isUserLoggedIn());
        assertThrows(UserAlreadyLoggedInException.class, () -> authentication.authenticate(testUser.getEmail(), testUser.getPassword()));
    }

    @Test
    @Order(3)
    void testEmailCheck() {
        assertThrows(EmailEmptyException.class, () -> authentication.emailCheck(" "));
        assertThrows(EmailAlreadyInUseException.class, () -> authentication.emailCheck(testUser.getEmail()));
        assertDoesNotThrow(() -> authentication.emailCheck("new-email@emails.com"));
        assertThrows(InvalidEmailFormatException.class, () -> authentication.emailCheck("johnwick"));
    }

    @Test
    @Order(4)
    void testResetPassword() {
        assertThrows(EmailEmptyException.class, () -> authentication.resetPassword(" ", testUser.getPassword()));
        assertThrows(PasswordEmptyException.class, () -> authentication.resetPassword(testUser.getEmail(), " "));
        assertThrows(InvalidEmailFormatException.class, () -> authentication.resetPassword("johnwick", testUser.getPassword()));
        assertThrows(InvalidPasswordFormatException.class, () -> authentication.resetPassword(testUser.getEmail(), "VerySecure"));
        assertThrows(UserNotFoundException.class, () -> authentication.resetPassword("HelloAndyOneThere@gmail.com.au", testUser.getPassword()));
        assertDoesNotThrow(()-> authentication.resetPassword(testUser.getEmail(), "MyDogBirthday093j-kd!asdfoiajslaksndf"));
        User user = userDAO.getUser(testUser.getEmail());
        assertEquals(PasswordUtils.hashSHA256("MyDogBirthday093j-kd!asdfoiajslaksndf"), user.getPassword());
    }

    @Test
    @Order(5)
    void testCheckSecurityQuestion() {
       assertThrows(EmptyAnswerException.class, () -> authentication.checkSecurityQuestion(testUser, " ", testQuestions.getAnswerTwo(),testQuestions.getAnswerThree()));
       assertThrows(EmptyAnswerException.class, () -> authentication.checkSecurityQuestion(testUser, testQuestions.getAnswerOne(), " ",testQuestions.getAnswerThree()));
       assertThrows(EmptyAnswerException.class, () -> authentication.checkSecurityQuestion(testUser, testQuestions.getAnswerOne(), testQuestions.getAnswerTwo()," "));
       questionDAO.createQuestion(testQuestions);
       assertDoesNotThrow(()-> authentication.checkSecurityQuestion(ApplicationState.getCurrentUser(), testQuestions.getAnswerOne(), testQuestions.getAnswerTwo(),testQuestions.getAnswerThree()));
       assertThrows(FailedQuestionException.class, () -> authentication.checkSecurityQuestion(testUser, testQuestions.getAnswerOne(), " awdawd",testQuestions.getAnswerThree()));
    }
}
