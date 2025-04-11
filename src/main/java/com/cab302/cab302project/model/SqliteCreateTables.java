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
}
