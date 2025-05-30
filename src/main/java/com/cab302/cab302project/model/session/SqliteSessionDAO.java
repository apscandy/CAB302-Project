package com.cab302.cab302project.model.session;

import com.cab302.cab302project.model.SqliteConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;



/**
 * DAO implementation for managing sessions using SQLite database.
 * This class handles creating, ending sessions, and managing session results.
 */
public class SqliteSessionDAO implements ISessionDAO {

    private final Connection con;

    private final String CREATE_SESSION_QUERY = "INSERT INTO session (user_id, deck_id, start_date_time, session_type) VALUES (?, ?, ?, ?)";
    private final String END_SESSION_QUERY = "UPDATE session SET end_date_time = ?, session_finished = ? WHERE id = ?";
    private  final String CREATE_SESSION_RESULTS_QUERY = "INSERT INTO session_results (id, card_id) VALUES (?, ?)";
    private final String UPDATE_SESSION_RESULTS_QUERY = "UPDATE session_results SET correct = ?, incorrect = ?, time_to_answer = ? WHERE id = ? AND card_id = ?";

    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Constructs a new SqliteSessionDAO instance.
     * Initializes the database connection.
     */
    public SqliteSessionDAO() {
        this.con = SqliteConnection.getInstance();
    }

    /**
     * Creates a new session in the database.
     *
     * @param session The Session object containing details for the new session.
     *                Note: This method will set the test mode of the session.
     * @throws RuntimeException if an error occurs during session creation.
     */
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

    /**
     * Ends an existing session by updating its end time and finished status.
     *
     * @param session The Session object containing updated details for the session.
     *                Note: This method will set the end date time and session finished flag.
     * @throws RuntimeException if an error occurs during session ending.
     */
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

    /**
     * Creates a new session result entry in the database.
     *
     * @param sessionResults The SessionResults object containing details for the result.
     *                       Note: This object must already have an ID assigned.
     * @throws RuntimeException if an error occurs during creation of the session result.
     */
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
            }
            finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    /**
     * Updates a session result to mark the card as correct, setting the time taken to answer.
     *
     * @param sessionResults   The SessionResults object containing updated result details.
     * @param startTime        The start time of the session when the card was answered.
     * @param endTime          The end time of the session when the card was answered.
     * @throws RuntimeException if an error occurs during update of the session result.
     */
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


    /**
     * Updates a session result to mark the card as incorrect, setting the time taken to answer.
     *
     * @param sessionResults   The SessionResults object containing updated result details.
     * @param startTime        The start time of the session when the card was answered.
     * @param endTime          The end time of the session when the card was answered.
     * @throws RuntimeException if an error occurs during update of the session result.
     */
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
