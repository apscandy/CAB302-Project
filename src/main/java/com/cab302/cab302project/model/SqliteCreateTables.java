package com.cab302.cab302project.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Statement;

public class SqliteCreateTables {
    private static final Logger logger = LogManager.getLogger(SqliteCreateTables.class);
    private final Connection con;

    public SqliteCreateTables() {
        con = SqliteConnection.getInstance();
        createUserTable();
        createUserEmailIndex();
        createDeckTable();
        createCardTable();
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
        }catch (Exception e){
            logger.fatal("Error creating user table: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void createDeckTable() {
        try {
            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS deck ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "user_id INTEGER NOT NULL,"
                    + "name TEXT NOT NULL,"
                    + "description TEXT NOT NULL,"
                    + "FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE"
                    + ")";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            logger.fatal("Error creating deck table: {}", e.getMessage());
        }
    }

    private void createUserEmailIndex() {
        try {
            Statement stmt = con.createStatement();
            String sql = "CREATE UNIQUE INDEX IF NOT EXISTS idx_user_email ON user (email)";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            logger.fatal("Error creating user email index: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void createCardTable() {
        try {
            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS card ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "deck_id INTEGER NOT NULL,"
                    + "question TEXT NOT NULL,"
                    + "answer TEXT NOT NULL,"
                    + "FOREIGN KEY (deck_id) REFERENCES deck(id) ON DELETE CASCADE"
                    + ")";
            stmt.executeUpdate(sql);
        }catch (Exception e){
            logger.fatal("Error creating card table: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
