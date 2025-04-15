package com.cab302.cab302project.controller;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.controller.user.AuthenticationService;
import com.cab302.cab302project.error.UserAlreadyLoggedInException;
import com.cab302.cab302project.error.authentication.*;
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

/**
 * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthenticationServiceTest {

    private static Connection con;
    private static SqliteUserDAO userDAO;
    private static SqliteUserSecurityQuestionDAO questionDAO;
    private static User testUser;
    private static UserSecurityQuestion testQuestions;
    private static AuthenticationService authenticationService;

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
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
        authenticationService = new AuthenticationService();
        ApplicationState.logout();
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
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
            ApplicationState.logout();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ApplicationState.logout();
    }


    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Test
    @Order(1)
    void testRegisterUser() {

        testUser.setPassword("");
        assertThrows(PasswordEmptyException.class, ()-> authenticationService.register(testUser));

        testUser.setEmail("");
        assertThrows(EmailEmptyException.class, () -> authenticationService.register(testUser));

        testUser.setEmail("johnwick");
        testUser.setPassword("MyDogBirthday093j-kd!");
        assertThrows(InvalidEmailFormatException.class, () -> authenticationService.register(testUser));

        testUser.setEmail("johnwick@malicious.com.ru");
        testUser.setPassword("VerySecure");
        assertThrows(InvalidPasswordFormatException.class, () -> authenticationService.register(testUser));

        testUser.setPassword("MyDogBirthday093j-kd!");
        testUser.setEmail("johnwick@malicious.com");
        boolean result = authenticationService.register(testUser);
        assertTrue(result);
        assertThrows(EmailAlreadyInUseException.class, () -> authenticationService.register(testUser));
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Test
    @Order(2)
    void testAuthenticateUser() {
        assertFalse(ApplicationState.isUserLoggedIn());
        assertThrows(PasswordEmptyException.class, () -> authenticationService.authenticate(testUser.getEmail(), ""));
        assertThrows(EmailEmptyException.class, () -> authenticationService.authenticate("", testUser.getPassword()));
        assertThrows(UserNotFoundException.class, () -> authenticationService.authenticate("jimbob@emails.com", testUser.getPassword()));
        assertThrows(InvalidEmailFormatException.class, () -> authenticationService.authenticate("johnwick", testUser.getPassword()));
        assertThrows(PasswordComparisonException.class, () -> authenticationService.authenticate(testUser.getEmail(), "MyDogBirthday093j-kd!asdfoiajs"));
        assertDoesNotThrow(()-> authenticationService.authenticate(testUser.getEmail(), "MyDogBirthday093j-kd!"));
        assertTrue(ApplicationState.isUserLoggedIn());
        assertThrows(UserAlreadyLoggedInException.class, () -> authenticationService.authenticate(testUser.getEmail(), testUser.getPassword()));
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Test
    @Order(3)
    void testEmailCheck() {
        assertThrows(EmailEmptyException.class, () -> authenticationService.emailCheck(" "));
        assertThrows(EmailAlreadyInUseException.class, () -> authenticationService.emailCheck(testUser.getEmail()));
        assertDoesNotThrow(() -> authenticationService.emailCheck("new-email@emails.com"));
        assertThrows(InvalidEmailFormatException.class, () -> authenticationService.emailCheck("johnwick"));
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Test
    @Order(4)
    void testResetPassword() {
        assertThrows(EmailEmptyException.class, () -> authenticationService.resetPassword(" ", testUser.getPassword()));
        assertThrows(PasswordEmptyException.class, () -> authenticationService.resetPassword(testUser.getEmail(), " "));
        assertThrows(InvalidEmailFormatException.class, () -> authenticationService.resetPassword("johnwick", testUser.getPassword()));
        assertThrows(InvalidPasswordFormatException.class, () -> authenticationService.resetPassword(testUser.getEmail(), "VerySecure"));
        assertThrows(UserNotFoundException.class, () -> authenticationService.resetPassword("HelloAndyOneThere@gmail.com.au", testUser.getPassword()));
        assertDoesNotThrow(()-> authenticationService.resetPassword(testUser.getEmail(), "MyDogBirthday093j-kd!asdfoiajslaksndf"));
        User user = userDAO.getUser(testUser.getEmail());
        assertEquals(PasswordUtils.hashSHA256("MyDogBirthday093j-kd!asdfoiajslaksndf"), user.getPassword());
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Test
    @Order(5)
    void testCheckSecurityQuestion() {
       assertThrows(EmptyAnswerException.class, () -> authenticationService.checkSecurityQuestion(testUser, " ", testQuestions.getAnswerTwo()));
       assertThrows(EmptyAnswerException.class, () -> authenticationService.checkSecurityQuestion(testUser, testQuestions.getAnswerOne(), " "));

       questionDAO.createQuestion(testQuestions);
       assertDoesNotThrow(()-> authenticationService.checkSecurityQuestion(ApplicationState.getCurrentUser(), testQuestions.getAnswerOne(), testQuestions.getAnswerTwo()));
       assertThrows(FailedQuestionException.class, () -> authenticationService.checkSecurityQuestion(testUser, testQuestions.getAnswerOne(), " awdawd"));
    }
}
