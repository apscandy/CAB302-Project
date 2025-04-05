package com.cab302.cab302project.model;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteConnection {
    private static final Logger logger = LogManager.getLogger(SqliteConnection.class);
    private static Connection instance = null;
    private static Boolean testingMode = false;


    /**
     * Refactored the week 6 practical code
     * to allow for an in memory database for testing.
     * This allows testing to have an ephemeral database
     * that produces no artifacts that the developer need
     * to remember to delete before each test.
     * <p>
     * in order for the in memory database to work
     * before anything happens call setTestingModeTrue()
     * in the testing class
     * <p>
     *  I have set strict mode to enforce database types,
     *  Not null and keys in the database.
     * <P>
     * https://www.sqlite.org/inmemorydb.html
     * <p>
     * https://sqlite.org/src/wiki/StrictMode
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    private SqliteConnection()  {
        String url;
        if (testingMode) {
            url = "jdbc:sqlite::memory:";
        }else{
            url = "jdbc:sqlite:flashCards.db";
        }
        logger.debug("Database connection string is: {}", url);
        try{
            instance = DriverManager.getConnection(url);
            Statement stmt = instance.createStatement();
            String sql = "PRAGMA foreign_keys = ON; PRAGMA strict = ON; PRAGMA synchronous = FULL; PRAGMA locking_mode = EXCLUSIVE; PRAGMA temp_store = MEMORY;";
            stmt.executeUpdate(sql);
            logger.info("Database connection established.");
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
        }
    }

    public static Connection getInstance() {
        if (instance == null) {
            new SqliteConnection();
        }
        return instance;
    }

    public static void setTestingModeTrue() {
        logger.debug("Testing mode is set to true");
        testingMode = true;
    }
}
