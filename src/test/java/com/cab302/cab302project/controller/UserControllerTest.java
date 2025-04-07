package com.cab302.cab302project.controller;

import com.cab302.cab302project.model.SqliteConnection;
import com.cab302.cab302project.model.SqliteCreateTables;
import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.user.User;
import com.cab302.cab302project.model.userSecQuestions.SqliteUserSecurityQuestionDAO;
import com.cab302.cab302project.model.userSecQuestions.UserSecurityQuestion;
import org.junit.jupiter.api.*;

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
}
