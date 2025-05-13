package com.cab302.cab302project.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class SqliteCreateTables {
    private static final Logger logger = LogManager.getLogger(SqliteCreateTables.class);
    private final Connection con;

    public SqliteCreateTables() {
        con = SqliteConnection.getInstance();
        try {
            createUserTable();
            createUserEmailIndex();
            createUserSecurityQuestionTable();
            createDeckTable();
            createCardTable();
            creatSessionTable();
            creatSessionResultsTable();
            creatEventsTable();
            createCardResultsSummaryView();
        }catch (RuntimeException e) {
            logger.error(e.getMessage());
            logger.error("SqliteCreateTables error");
        }
    }

    /**
     * This function is intentionally made private to force developers
     * to use the constructor at application start
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     * @throws RuntimeException if the table failed to create
     * There is no point allowing the user to continue
     */
    private void createUserTable() {
        try{
            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS user ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "first_name TEXT NOT NULL,"
                    + "last_name TEXT NOT NULL,"
                    + "email TEXT NOT NULL UNIQUE,"
                    + "password TEXT NOT NULL"
                    + ")";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    private void createDeckTable() {
        try {
            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS deck ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "user_id INTEGER NOT NULL,"
                    + "name TEXT NOT NULL,"
                    + "description TEXT NOT NULL,"
                    + "is_deleted BOOLEAN NOT NULL DEFAULT false,"
                    + "is_bookmarked BOOLEAN NOT NULL DEFAULT false,"
                    + "FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE"
                    + ")";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e){
            logger.error( e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    private void createUserEmailIndex() {
        try {
            Statement stmt = con.createStatement();
            String sql = "CREATE UNIQUE INDEX IF NOT EXISTS idx_user_email ON user (email)";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    private void createCardTable() {
        try {
            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS card ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "deck_id INTEGER NOT NULL,"
                    + "question TEXT NOT NULL,"
                    + "answer TEXT NOT NULL,"
                    + "tags TEXT,"
                    + "is_deleted BOOLEAN NOT NULL DEFAULT false,"
                    + "FOREIGN KEY (deck_id) REFERENCES deck(id) ON DELETE CASCADE"
                    + ")";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e){
            logger.error( e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    private void createUserSecurityQuestionTable() {
        try{
            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS user_security_question ("
                    + "id INTEGER PRIMARY KEY,"
                    + "sec_question_one TEXT NOT NULL,"
                    + "sec_answer_one TEXT NOT NULL,"
                    + "sec_question_two TEXT NOT NULL,"
                    + "sec_answer_two TEXT NOT NULL,"
                    + "sec_question_three TEXT NOT NULL,"
                    + "sec_answer_three TEXT NOT NULL,"
                    + "FOREIGN KEY(id) REFERENCES user(id) ON DELETE CASCADE"
                    + ")";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    private void creatSessionTable() {
        try {
            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS session ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "user_id INTEGER NOT NULL,"
                    + "deck_id INTEGER NOT NULL,"
                    + "session_type TEXT NOT NULL,"
                    + "start_date_time TEXT NOT NULL,"
                    + "end_date_time TEXT,"
                    + "session_finished BOOLEAN NOT NULL DEFAULT 0, "
                    + "FOREIGN KEY(user_id) REFERENCES user(id) ON DELETE CASCADE,"
                    + "FOREIGN KEY (deck_id) REFERENCES deck(id) ON DELETE CASCADE"
                    + ")";
            stmt.executeUpdate(sql);
            stmt.close();
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    private void creatSessionResultsTable(){
        try {
            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS session_results ("
                    + "id INTEGER NOT NULL,"
                    + "card_id INTEGER NOT NULL,"
                    + "correct BOOLEAN NOT NULL DEFAULT false,"
                    + "incorrect BOOLEAN NOT NULL DEFAULT false,"
                    + "time_to_answer TEXT,"
                    + "PRIMARY KEY (id, card_id),"
                    + "FOREIGN KEY(id) REFERENCES session(id) ON DELETE CASCADE,"
                    + "FOREIGN KEY(card_id) REFERENCES card(id) ON DELETE CASCADE"
                    + ")";
            stmt.executeUpdate(sql);
            stmt.close();
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    private void creatEventsTable(){
        try {
            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS events ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "user_id INTEGER NOT NULL,"
                    + "event_message TEXT NOT NULL DEFAULT 'NO EVENT',"
                    + "date_time TEXT NOT NULL,"
                    + "FOREIGN KEY(user_id) REFERENCES user(id) ON DELETE CASCADE"
                    + ")";
            stmt.executeUpdate(sql);
            stmt.close();
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    private void createCardResultsSummaryView() {
        try {
            Statement stmt = con.createStatement();
            String sql = "CREATE VIEW IF NOT EXISTS card_results_summary AS " +
                    "SELECT card_id, " +
                    "SUM(CASE WHEN correct THEN 1 ELSE 0 END) AS correct_count, " +
                    "SUM(CASE WHEN incorrect THEN 1 ELSE 0 END) AS incorrect_count " +
                    "FROM session_results " +
                    "GROUP BY card_id";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
