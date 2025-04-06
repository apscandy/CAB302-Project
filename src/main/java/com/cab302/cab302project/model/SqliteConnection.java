package com.cab302.cab302project.model;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;


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
public class SqliteConnection {
    private static final Logger logger = LogManager.getLogger(SqliteConnection.class);
    private static Connection instance = null;
    private static Boolean testingMode = false;

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
            setDataBasePragma(instance);
            checkPragma(instance);
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

    private static void setDataBasePragma(Connection instance) {
        logger.debug("Setting database pragma");
        try {
            Statement stmt = instance.createStatement();
            String sql;
            sql = "PRAGMA strict = ON;";
            stmt.executeUpdate(sql);
            sql = "PRAGMA foreign_keys = ON;";
            stmt.executeUpdate(sql);
            sql = "PRAGMA synchronous = FULL;";
            stmt.executeUpdate(sql);
            sql = "PRAGMA locking_mode = EXCLUSIVE;";
            stmt.executeUpdate(sql);
            stmt.close();
            logger.debug("database pragma set successfully.");
        }catch (SQLException e){
            logger.fatal(e.getMessage());
        }
    }

    private static void checkPragma(Connection instance) {
        try {
            Statement stmt = instance.createStatement();
            String sql;

            sql = "PRAGMA foreign_keys;";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                int foreignKeysEnabled = rs.getInt(1);  // Returns 1 if enabled, 0 if not
                logger.debug("Foreign keys enabled: {}", foreignKeysEnabled);
            }
            rs.close();

            sql = "PRAGMA synchronous;";
            ResultSet rs1 = stmt.executeQuery(sql);
            if (rs1.next()) {
                int synchronousMode = rs1.getInt(1);  // Values: 0 (OFF), 1 (NORMAL), 2 (FULL)
                logger.info("Synchronous Mode: {}", synchronousMode);
            }
            rs1.close();

            sql = "PRAGMA locking_mode;";
            ResultSet rs2 = stmt.executeQuery(sql);
            if (rs2.next()) {
                String lockingMode = rs2.getString(1);  // Values: "NORMAL", "EXCLUSIVE"
                logger.info("Locking Mode: {}", lockingMode);
            }
            rs2.close();
            stmt.close();
        }catch (SQLException e){
            logger.fatal(e.getMessage());
        }catch (Exception E){
            logger.fatal(E);
        }
    }
}
