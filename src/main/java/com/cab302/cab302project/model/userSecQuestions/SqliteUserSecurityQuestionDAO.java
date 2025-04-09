package com.cab302.cab302project.model.userSecQuestions;

import com.cab302.cab302project.model.SqliteConnection;
import com.cab302.cab302project.model.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class SqliteUserSecurityQuestionDAO implements IUserSecurityQuestionDAO {

    private final Connection conn;
    private final Logger logger = LogManager.getLogger(SqliteUserSecurityQuestionDAO.class);

    private final String createUserSecurityQuestionSQL = "INSERT INTO user_security_question (id, sec_question_one, sec_answer_one, sec_question_two, sec_answer_two, sec_question_three, sec_answer_three) VALUES (?,?,?,?,?,?,?)";
    private final String getUserSecurityQuestionSQL = "SELECT * FROM user_security_question WHERE id = ?";
    private final String updateUserSecurityQuestionSQL = "UPDATE user_security_question SET sec_question_one = ?, sec_answer_one = ?, sec_question_two = ?, sec_answer_two = ?, sec_question_three = ?, sec_answer_three = ? WHERE id = ?;";

    public SqliteUserSecurityQuestionDAO() {
        conn = SqliteConnection.getInstance();
    }

    @Override
    public void createQuestion(UserSecurityQuestion question) {
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(createUserSecurityQuestionSQL)) {
                stmt.setInt(1, question.getUserId());
                stmt.setString(2, question.getQuestionOne());
                stmt.setString(3, question.getAnswerOne());
                stmt.setString(4, question.getQuestionTwo());
                stmt.setString(5, question.getAnswerTwo());
                stmt.setString(6, question.getQuestionThree());
                stmt.setString(7, question.getAnswerThree());
                stmt.executeUpdate();
                conn.commit();
                stmt.close();
                logger.info("Create user security questions transaction completed successfully.");
            }catch (SQLException  e) {
                conn.rollback();
                logger.error("Created user security question transaction failed: {}", e.getMessage());
                logger.fatal(e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        }catch (Exception e) {
            logger.fatal(e.getMessage());
        }
    }

    @Override
    public UserSecurityQuestion getQuestions(User user) {
        UserSecurityQuestion userQuestions = new UserSecurityQuestion(user);
        try {
            conn.setAutoCommit(false);
            try(PreparedStatement statement = conn.prepareStatement(getUserSecurityQuestionSQL)){
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
                rs.close();
                statement.close();
            }catch (SQLException e) {
                conn.rollback();
                logger.error("Get user security question transaction failed: {}", e.getMessage());
                logger.fatal(e.getMessage());

            }finally {
                conn.setAutoCommit(true);
            }
        }catch (Exception e) {
            logger.fatal(e.getMessage());
        }
        return userQuestions;
    }

    @Override
    public void updateQuestions(UserSecurityQuestion updatedQuestion) {
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(updateUserSecurityQuestionSQL)) {
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
                logger.error("update user security question transaction failed: {}", e.getMessage());
                logger.fatal(e.getMessage());
            }finally {
                conn.setAutoCommit(true);
            }
        }catch (Exception e) {
            logger.fatal(e.getMessage());
        }

    }
}
