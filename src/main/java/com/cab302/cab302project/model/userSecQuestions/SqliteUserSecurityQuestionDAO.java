package com.cab302.cab302project.model.userSecQuestions;

import com.cab302.cab302project.error.model.question.FailedToCreateQuestionsException;
import com.cab302.cab302project.error.model.question.FailedToGetQuestionsException;
import com.cab302.cab302project.error.model.question.FailedToUpdateQuestionsException;
import com.cab302.cab302project.model.SqliteConnection;
import com.cab302.cab302project.model.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * SQLite data access object for user security questions management.
 * <p>
 * Provides database operations for creating, retrieving, and updating
 * user security questions and answers. Handles three security questions
 * per user with their corresponding answers, storing them securely in
 * the user_security_question table. All operations use database
 * transactions with proper rollback handling for data integrity.
 * Implements the IUserSecurityQuestionDAO interface for consistent
 * data access patterns.
 * </p>
 * @author Hoang Dat Bui (n11659831, hoangdat.bui@connect.qut.edu.au)
 **/
public final class SqliteUserSecurityQuestionDAO implements IUserSecurityQuestionDAO {

    private final Connection conn;
    private final Logger logger = LogManager.getLogger(SqliteUserSecurityQuestionDAO.class);

    private final String CREATE_USQ_SQL = "INSERT INTO user_security_question (id, sec_question_one, sec_answer_one, sec_question_two, sec_answer_two, sec_question_three, sec_answer_three) VALUES (?,?,?,?,?,?,?)";
    private final String GET_USQ_SQL = "SELECT * FROM user_security_question WHERE id = ?";
    private final String UPDATE_USQ_SQL = "UPDATE user_security_question SET sec_question_one = ?, sec_answer_one = ?, sec_question_two = ?, sec_answer_two = ?, sec_question_three = ?, sec_answer_three = ? WHERE id = ?;";

    public SqliteUserSecurityQuestionDAO() {
        conn = SqliteConnection.getInstance();
    }

    /**
     * Stores a new set of security questions and answers for a user.
     * Inserts the user ID, questions, and answers into the user_security_question table.
     *
     * @param question The UserSecurityQuestion object containing all question and answer data
     * @throws FailedToCreateQuestionsException if database insertion fails
     * @author Hoang Dat Bui (hoangdat.bui@connect.qut.edu.au), Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    @Override
    public void createQuestion(UserSecurityQuestion question) {
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(CREATE_USQ_SQL)) {
                stmt.setInt(1, question.getUserId());
                stmt.setString(2, question.getQuestionOne());
                stmt.setString(3, question.getAnswerOne());
                stmt.setString(4, question.getQuestionTwo());
                stmt.setString(5, question.getAnswerTwo());
                stmt.setString(6, question.getQuestionThree());
                stmt.setString(7, question.getAnswerThree());
                stmt.executeUpdate();
                conn.commit();
            }catch (SQLException  e) {
                conn.rollback();
                logger.error(e.getMessage());
                throw new FailedToCreateQuestionsException(e.getMessage());
            }finally {
                conn.setAutoCommit(true);
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToCreateQuestionsException(e.getMessage());
        }
    }

    /**
     * Retrieves security questions and answers for a specified user.
     * <p>
     * Creates a UserSecurityQuestion object and populates it with data from the database
     * using the user's ID. Retrieves all three security questions and their corresponding
     * answers in a single database query. Uses transaction management to ensure data
     * consistency during retrieval operations.
     * </p>
     *
     * @param user The User object whose security questions are being retrieved
     * @return A populated UserSecurityQuestion object with all questions and answers
     * @throws FailedToGetQuestionsException if database retrieval fails
     * @author Hoang Dat Bui (hoangdat.bui@connect.qut.edu.au), Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    @Override
    public UserSecurityQuestion getQuestions(User user) {
        UserSecurityQuestion userQuestions = new UserSecurityQuestion(user);
        try {
            conn.setAutoCommit(false);
            try(PreparedStatement statement = conn.prepareStatement(GET_USQ_SQL)){
                statement.setInt(1, userQuestions.getUserId());
                conn.commit();
                ResultSet rs = statement.executeQuery();
                if(rs.next()) {
                    String questionOne = rs.getString("sec_question_one");
                    String questionTwo = rs.getString("sec_question_two");
                    String questionThree = rs.getString("sec_question_three");
                    String answerOne = rs.getString("sec_answer_one");
                    String answerTwo = rs.getString("sec_answer_two");
                    String answerThree = rs.getString("sec_answer_three");
                    userQuestions.setQuestionOne(questionOne);
                    userQuestions.setQuestionTwo(questionTwo);
                    userQuestions.setQuestionThree(questionThree);
                    userQuestions.setAnswerOne(answerOne);
                    userQuestions.setAnswerTwo(answerTwo);
                    userQuestions.setAnswerThree(answerThree);

                }
                conn.commit();
                statement.close();
                rs.close();
            }catch (SQLException e) {
                conn.rollback();
                logger.error(e.getMessage());
                throw new FailedToGetQuestionsException(e.getMessage());
            }finally {
                conn.setAutoCommit(true);
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToGetQuestionsException(e.getMessage());
        }
        return userQuestions;
    }

    /**
     * Updates existing security questions and answers for a user.
     * <p>
     * Modifies all question and answer fields in the database for the specified user ID
     * using a single UPDATE statement. All three security questions and answers are
     * updated atomically within a database transaction. Rolls back changes if any
     * error occurs during the update process to maintain data integrity.
     * </p>
     *
     * @param updatedQuestion The UserSecurityQuestion object containing updated data
     * @throws FailedToUpdateQuestionsException if database update fails
     * @author Hoang Dat Bui (hoangdat.bui@connect.qut.edu.au), Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    @Override
    public void updateQuestions(UserSecurityQuestion updatedQuestion){
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_USQ_SQL)) {
                pstmt.setString(1, updatedQuestion.getQuestionOne());
                pstmt.setString(2, updatedQuestion.getAnswerOne());
                pstmt.setString(3, updatedQuestion.getQuestionTwo());
                pstmt.setString(4, updatedQuestion.getAnswerTwo());
                pstmt.setString(5, updatedQuestion.getQuestionThree());
                pstmt.setString(6, updatedQuestion.getAnswerThree());
                pstmt.setInt(7, updatedQuestion.getUserId());
                pstmt.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                logger.error(e.getMessage());
                throw new FailedToUpdateQuestionsException(e.getMessage());
            }finally {
                conn.setAutoCommit(true);
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToUpdateQuestionsException(e.getMessage());
        }

    }
}
