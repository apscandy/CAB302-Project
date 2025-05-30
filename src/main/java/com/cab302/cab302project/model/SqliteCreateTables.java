package com.cab302.cab302project.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class to create SQLite tables for the CAB302 project database.
 * This class ensures that all required database tables are created when the application starts.
 * It is designed as a singleton pattern with a private constructor to enforce usage via the single instance.
 *
 * <p>Key features:
 * - Ensures tables are created if they do not exist.
 * - Uses foreign key constraints and indexes for data integrity.
 * - Logs errors during table creation using log4j.
 * - Throws RuntimeException on any database error to prevent application continuation.</p>
 *
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public final class SqliteCreateTables {
    private static final Logger logger = LogManager.getLogger(SqliteCreateTables.class);
    private final Connection con;


    /**
     * Default constructor that initializes the connection to the SQLite database
     * and creates all required tables on application startup.
     *
     * <p>This method is intended for use at application initialization to ensure the database schema is in place.
     * It calls private methods to create each table if it does not already exist.</p>
     *
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     * @throws RuntimeException If any table creation fails, preventing the application from continuing.
     */
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
        }catch (RuntimeException e) {
            logger.error(e.getMessage());
            logger.error("SqliteCreateTables error");
        }
    }

    /**
     * Creates the "user" table in the SQLite database if it does not already exist.
     * The table stores user information including first name, last name, email (unique), and password.
     *
     * <p>Schema:
     * - id: Auto-incrementing primary key.
     * - first_name: Required text field.
     * - last_name: Required text field.
     * - email: Unique text field with a separate index for faster queries.
     * - password: Required text field.</p>
     *
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     * @throws RuntimeException If the table creation fails (e.g., duplicate table, SQL error).
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
     * Creates the "deck" table in the SQLite database if it does not already exist.
     * This table stores information about user-created decks, including associated cards and metadata.
     *
     * <p>Schema:
     * - id: Auto-incrementing primary key.
     * - user_id: Foreign key referencing the "user" table (ON DELETE CASCADE).
     * - name: Required text field for deck title.
     * - description: Text field for additional details.
     * - is_deleted: Boolean flag to mark decks as deleted (default false).
     * - is_bookmarked: Boolean flag to track if a deck is bookmarked (default false).</p>
     *
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     * @throws RuntimeException If the table creation fails (e.g., duplicate table, SQL error).
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
     * Creates a unique index on the "email" column of the "user" table to enforce uniqueness.
     *
     * <p>This ensures that no two users can have the same email address, improving query performance
     * for lookups and preventing duplicate entries.</p>
     *
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     * @throws RuntimeException If the index creation fails (e.g., duplicate index, SQL error).
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
     * Creates the "card" table in the SQLite database if it does not already exist.
     * This table stores individual flashcards associated with a deck.
     *
     * <p>Schema:
     * - id: Auto-incrementing primary key.
     * - deck_id: Foreign key referencing the "deck" table (ON DELETE CASCADE).
     * - question: Required text field for the card's question.
     * - answer: Required text field for the card's answer.
     * - tags: Optional comma-separated text field for categorization.
     * - is_deleted: Boolean flag to mark cards as deleted (default false).</p>
     *
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     * @throws RuntimeException If the table creation fails (e.g., duplicate table, SQL error).
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
     * Creates the "user_security_question" table in the SQLite database if it does not already exist.
     * This table stores user security questions and answers for account recovery purposes.
     *
     * <p>Schema:
     * - id: Foreign key referencing the "user" table (ON DELETE CASCADE).
     * - sec_question_one to sec_answer_three: Required text fields for three security questions and their answers.</p>
     *
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     * @throws RuntimeException If the table creation fails (e.g., duplicate table, SQL error).
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
     * Creates the "session" table in the SQLite database if it does not already exist.
     * This table tracks user study sessions, including session type and timestamps.
     *
     * <p>Schema:
     * - id: Auto-incrementing primary key.
     * - user_id: Foreign key referencing the "user" table (ON DELETE CASCADE).
     * - deck_id: Foreign key referencing the "deck" table (ON DELETE CASCADE).
     * - session_type: Required text field indicating the type of study session.
     * - start_date_time: Required timestamp for when the session started.
     * - end_date_time: Optional timestamp for when the session ended.
     * - session_finished: Boolean flag to indicate if the session is complete (default 0).</p>
     *
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     * @throws RuntimeException If the table creation fails (e.g., duplicate table, SQL error).
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
     * Creates the "session_results" table in the SQLite database if it does not already exist.
     * This table records user performance during study sessions (e.g., correct/incorrect answers).
     *
     * <p>Schema:
     * - id: Foreign key referencing the "session" table (ON DELETE CASCADE).
     * - card_id: Foreign key referencing the "card" table (ON DELETE CASCADE).
     * - correct: Boolean flag indicating if the answer was correct.
     * - incorrect: Boolean flag indicating if the answer was incorrect.
     * - time_to_answer: Text field for timing data (e.g., response duration).
     * - Composite primary key on (id, card_id) to prevent duplicate records.</p>
     *
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     * @throws RuntimeException If the table creation fails (e.g., duplicate table, SQL error).
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
     * Creates the "events" table in the SQLite database if it does not already exist.
     * This table logs user-related events such as login attempts or system alerts.
     *
     * <p>Schema:
     * - id: Auto-incrementing primary key.
     * - user_id: Foreign key referencing the "user" table (ON DELETE CASCADE).
     * - event_message: Required text field for event description (default 'NO EVENT').
     * - date_time: Required timestamp for when the event occurred.</p>
     *
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     * @throws RuntimeException If the table creation fails (e.g., duplicate table, SQL error).
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
}
