package com.cab302.cab302project.model.session;

import com.cab302.cab302project.model.SqliteConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class SqliteSessionDAO implements ISessionDAO {

    private final Connection con;

    private final String CREATE_SESSION_QUERY = "INSERT INTO session (user_id, deck_id, start_date_time, session_type) VALUES (?, ?, ?, ?)";
    private final String END_SESSION_QUERY = "UPDATE session SET end_date_time = ?, session_finished = ? WHERE id = ?";
    private  final String CREATE_SESSION_RESULTS_QUERY = "INSERT INTO session_results (id, card_id) VALUES (?, ?)";
    private final String UPDATE_SESSION_RESULTS_QUERY = "UPDATE session_results SET correct = ?, incorrect = ?, time_to_answer = ? WHERE id = ? AND card_id = ?";

    private final Logger logger = LogManager.getLogger(this.getClass());

    public SqliteSessionDAO() {
        this.con = SqliteConnection.getInstance();
    }

    @Override
    public void createSession(Session session) {
        session.setTestMode();
        try{
            con.setAutoCommit(false);
            try (PreparedStatement stmt = con.prepareStatement(CREATE_SESSION_QUERY)) {
                stmt.setInt(1, session.getUserId());
                stmt.setInt(2, session.getDeckId());
                stmt.setString(3, session.getStartDateTime().toString());
                stmt.setString(4, session.getTestMode().toString());
                stmt.executeUpdate();
                con.commit();
                ResultSet result = stmt.getGeneratedKeys();
                if (result.next()) {
                    session.setId(result.getInt(1));
                }
                stmt.close();
                result.close();
            }catch (SQLException e){
                con.rollback();
                logger.error(e.getMessage());
                throw new RuntimeException("");
            }
            finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void endSession(Session session) {
        session.setEndDateTime();
        session.setSessionFinished();
        try{
            con.setAutoCommit(false);
            try (PreparedStatement stmt = con.prepareStatement(END_SESSION_QUERY)) {
                stmt.setString(1, session.getEndDateTime().toString());
                stmt.setBoolean(2, session.getSessionFinished());
                stmt.setInt(3, session.getId());
                stmt.executeUpdate();
                con.commit();

            }catch (SQLException e){
                con.rollback();
                logger.error(e.getMessage());
                throw new RuntimeException("");
            }
            finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public void createSessionResult(SessionResults sessionResults) {
        try{
            con.setAutoCommit(false);
            try (PreparedStatement stmt = con.prepareStatement(CREATE_SESSION_RESULTS_QUERY)) {
                stmt.setInt(1, sessionResults.getId());
                stmt.setInt(2, sessionResults.getCardId());
                stmt.executeUpdate();
                con.commit();
            }catch (SQLException e){
                con.rollback();
                logger.error(e.getMessage());
                throw new RuntimeException("");
            }
            finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public void sessionResultCardCorrect(SessionResults sessionResults, LocalDateTime startTime, LocalDateTime endTime) {
        sessionResults.setCorrect(true);
        sessionResults.setTimeToAnswer(startTime, endTime);
        try{
            con.setAutoCommit(false);
            try (PreparedStatement stmt = con.prepareStatement(UPDATE_SESSION_RESULTS_QUERY)) {
                stmt.setBoolean(1, sessionResults.getCorrect());
                stmt.setBoolean(2, sessionResults.getIncorrect());
                stmt.setString(3, sessionResults.getTimeToAnswer());
                stmt.setInt(4, sessionResults.getId());
                stmt.setInt(5, sessionResults.getCardId());
                stmt.executeUpdate();
                con.commit();

            }catch (SQLException e){
                con.rollback();
                logger.error(e.getMessage());
                throw new RuntimeException("");
            }
            finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public void sessionResultCardIncorrect(SessionResults sessionResults, LocalDateTime startTime, LocalDateTime endTime) {
        sessionResults.setIncorrect(true);
        sessionResults.setTimeToAnswer(startTime, endTime);
        try{
            con.setAutoCommit(false);
            try (PreparedStatement stmt = con.prepareStatement(UPDATE_SESSION_RESULTS_QUERY)) {
                stmt.setBoolean(1, sessionResults.getCorrect());
                stmt.setBoolean(2, sessionResults.getIncorrect());
                stmt.setString(3, sessionResults.getTimeToAnswer());
                stmt.setInt(4, sessionResults.getId());
                stmt.setInt(5, sessionResults.getCardId());
                stmt.executeUpdate();
                con.commit();
            }catch (SQLException e){
                con.rollback();
                logger.error(e.getMessage());
                throw new RuntimeException("");
            }
            finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
