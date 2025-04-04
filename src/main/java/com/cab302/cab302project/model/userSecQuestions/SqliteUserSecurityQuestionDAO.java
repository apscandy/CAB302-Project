package com.cab302.cab302project.model.userSecQuestions;

import com.cab302.cab302project.model.SqliteConnection;
import java.sql.*;
import java.util.List;

public class SqliteUserSecurityQuestionDAO implements IUserSecurityQuestionDAO {

    private final Connection conn;

    public SqliteUserSecurityQuestionDAO() {
        conn = SqliteConnection.getInstance();
        createTable();
    }

    private void createTable() {
        try{
            Statement statement = conn.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS securityQuestions ("
                    + "id INTEGER PRIMARY KEY,"
                    + "secQuestionOne TEXT NOT NULL,"
                    + "secAnswerOne TEXT NOT NULL,"
                    + "secQuestionTwo TEXT NOT NULL,"
                    + "secAnswerTwo TEXT NOT NULL,"
                    + "secQuestionThree TEXT NOT NULL,"
                    + "secAnswerThree TEXT NOT NULL,"
                    + "FOREIGN KEY(id) REFERENCES users(id)"
                    + ")";
            statement.execute(query);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createQuestion(UserSecurityQuestion question) {
    }

    @Override
    public List<UserSecurityQuestion> getQuestions(int userId) {
        return null;
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
