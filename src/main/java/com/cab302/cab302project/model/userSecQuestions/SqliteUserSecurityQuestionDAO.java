package com.cab302.cab302project.model.userSecQuestions;

import com.cab302.cab302project.model.SqliteConnection;
import com.cab302.cab302project.model.user.User;

import java.sql.*;

public class SqliteUserSecurityQuestionDAO implements IUserSecurityQuestionDAO {

    private final Connection conn;

    public SqliteUserSecurityQuestionDAO() {
        conn = SqliteConnection.getInstance();
    }

    @Override
    public void createQuestion(UserSecurityQuestion question) {
        String sql = "INSERT INTO securityQuestions (id, secQuestionOne, secAnswerOne, secQuestionTwo, secAnswerTwo, secQuestionThree, secAnswerThree) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, question.getUserId());
            pstmt.setString(2, question.getQuestionOne());
            pstmt.setString(3, question.getAnswerOne());
            pstmt.setString(4, question.getQuestionTwo());
            pstmt.setString(5, question.getAnswerTwo());
            pstmt.setString(6, question.getQuestionThree());
            pstmt.setString(7, question.getAnswerThree());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserSecurityQuestion getQuestions(User user) {
        UserSecurityQuestion userQuestions = new UserSecurityQuestion(user);
        String sql = "SELECT secQuestionOne, secQuestionTwo, secQuestionThree FROM securityQuestions WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user.getId());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                userQuestions.setQuestionOne(rs.getString("secQuestionOne"));
                userQuestions.setQuestionTwo(rs.getString("secQuestionTwo"));
                userQuestions.setQuestionThree(rs.getString("secQuestionThree"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userQuestions;
    }

    @Override
    public void updateQuestions(UserSecurityQuestion updatedQuestion) {
        String sql = "UPDATE securityQuestions SET "
                + "secQuestionOne = ?, secAnswerOne = ?, "
                + "secQuestionTwo = ?, secAnswerTwo = ?, "
                + "secQuestionThree = ?, secAnswerThree = ? "
                + "WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, updatedQuestion.getQuestionOne());
            pstmt.setString(2, updatedQuestion.getAnswerOne());
            pstmt.setString(3, updatedQuestion.getQuestionTwo());
            pstmt.setString(4, updatedQuestion.getAnswerTwo());
            pstmt.setString(5, updatedQuestion.getQuestionThree());
            pstmt.setString(6, updatedQuestion.getAnswerThree());
            pstmt.setInt(7, updatedQuestion.getUserId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
