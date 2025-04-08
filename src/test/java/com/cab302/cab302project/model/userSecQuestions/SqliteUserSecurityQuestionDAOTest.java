package com.cab302.cab302project.model.userSecQuestions;

import com.cab302.cab302project.model.SqliteConnection;
import com.cab302.cab302project.model.SqliteCreateTables;
import com.cab302.cab302project.model.user.IUserDAO;
import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.user.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SqliteUserSecurityQuestionDAOTest {
    private static IUserSecurityQuestionDAO userSecurityQuestionDAO;
    private static IUserDAO userDAO;
    private static User user;
    private static UserSecurityQuestion userSecurityQuestion;
    private static Connection con;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        SqliteConnection.setTestingModeTrue();
        new SqliteCreateTables();
        userDAO = new SqliteUserDAO();
        userSecurityQuestionDAO = new SqliteUserSecurityQuestionDAO();
        con = SqliteConnection.getInstance();
        user = new User("Bob", "Joe", "bob@gmail.com", "password");
        userDAO.addUser(user);
        userSecurityQuestion = new UserSecurityQuestion(user);

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


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Order(1)
    void createQuestion() {
        userSecurityQuestion.setQuestionOne("What is your first name?");
        userSecurityQuestion.setQuestionTwo("What is your last name?");
        userSecurityQuestion.setQuestionThree("What is your email?");
        userSecurityQuestion.setAnswerOne("Bob");
        userSecurityQuestion.setAnswerTwo("Joe");
        userSecurityQuestion.setAnswerThree("bob@gmail.com");
        userSecurityQuestionDAO.createQuestion(userSecurityQuestion);
        var usqCheck = new UserSecurityQuestion(user);
        try{
            String sql = "SELECT * FROM  user_security_question WHERE id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, user.getId());
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                String questionOne = resultSet.getString("sec_question_one");
                String questionTwo = resultSet.getString("sec_question_two");
                String questionThree = resultSet.getString("sec_question_three");
                String answerOne = resultSet.getString("sec_answer_one");
                String answerTwo = resultSet.getString("sec_answer_two");
                String answerThree = resultSet.getString("sec_answer_three");
                usqCheck.setQuestionOne(questionOne);
                usqCheck.setQuestionTwo(questionTwo);
                usqCheck.setQuestionThree(questionThree);
                usqCheck.setAnswerOne(answerOne);
                usqCheck.setAnswerTwo(answerTwo);
                usqCheck.setAnswerThree(answerThree);
            }
            resultSet.close();
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(userSecurityQuestion.getUserId(), usqCheck.getUserId());
        assertEquals(userSecurityQuestion.getQuestionOne(), usqCheck.getQuestionOne());
        assertEquals(userSecurityQuestion.getQuestionTwo(), usqCheck.getQuestionTwo());
        assertEquals(userSecurityQuestion.getQuestionThree(), usqCheck.getQuestionThree());
        assertEquals(userSecurityQuestion.getAnswerOne(), usqCheck.getAnswerOne());
        assertEquals(userSecurityQuestion.getAnswerTwo(), usqCheck.getAnswerTwo());
        assertEquals(userSecurityQuestion.getAnswerThree(), usqCheck.getAnswerThree());
    }

    @Test
    @Order(2)
    void getQuestions() {
        var tmp = userSecurityQuestionDAO.getQuestions(user);
        assertEquals(userSecurityQuestion.getQuestionOne(), tmp.getQuestionOne());
        assertEquals(userSecurityQuestion.getQuestionTwo(), tmp.getQuestionTwo());
        assertEquals(userSecurityQuestion.getQuestionThree(), tmp.getQuestionThree());
        assertEquals(userSecurityQuestion.getAnswerOne(), tmp.getAnswerOne());
        assertEquals(userSecurityQuestion.getAnswerTwo(), tmp.getAnswerTwo());
        assertEquals(userSecurityQuestion.getAnswerThree(), tmp.getAnswerThree());

    }

    @Test
    @Order(3)
    void updateQuestions() {
        userSecurityQuestion.setQuestionOne("What is the meaning of life?");
        userSecurityQuestionDAO.updateQuestions(userSecurityQuestion);
        var temp = userSecurityQuestionDAO.getQuestions(user);
        assertEquals(userSecurityQuestion.getQuestionOne(), temp.getQuestionOne());

    }
}