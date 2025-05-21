package com.cab302.cab302project.model.session;

import com.cab302.cab302project.model.SqliteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SqliteSessionResultsDAO {
    private static final Logger logger = LogManager.getLogger(SqliteSessionResultsDAO.class);
    private final Connection con;

    public SqliteSessionResultsDAO() {
        this.con = SqliteConnection.getInstance();
    }

    public Map<Integer, double[]> getCardResultsForUser(int userId) {
        String query = """
                SELECT sr.card_id,
                   SUM(CASE WHEN sr.correct THEN 1 ELSE 0 END) as correct_count,
                   SUM(CASE WHEN sr.incorrect THEN 1 ELSE 0 END) as incorrect_count
                FROM session_results sr
                JOIN session s ON sr.id = s.id
                WHERE s.user_id = ?
                GROUP BY sr.card_id
                """;
        Map<Integer, double[]> statsMap = new HashMap<>();
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int cardId = rs.getInt("card_id");
                int correct = rs.getInt("correct_count");
                int incorrect = rs.getInt("incorrect_count");
                statsMap.put(cardId, new double[]{correct, incorrect});
            }
        } catch (Exception e) {
            logger.error("Error retrieving session stats: " + e.getMessage());
        }
        return statsMap;
    }
}

