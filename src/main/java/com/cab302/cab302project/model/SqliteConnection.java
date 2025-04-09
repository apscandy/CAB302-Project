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
 * <a href="https://www.sqlite.org/inmemorydb.html">Sqlite in-memory</a>
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

    /**
     * This sets the testingMode to true, must be called before
     * getInstance method otherwise the database will operate as
     * normal.
     * <p>
     * This can be used in TestClass's to run real sqlite queries
     * without producing a database file.
     * This also mean that once the tests are finished any data in
     * the database is cleared, making the database ephemeral.
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     */
    public static void setTestingModeTrue() {
        logger.debug("Testing mode is set to true");
        testingMode = true;
    }

    /**
     * Strict mode enforces types in the database, under normal mode you can put a string into
     * an integer column and primary keys are allowed to be null.
     * With strict mode on it forces types and constants on primary keys.
     * <a href="https://sqlite.org/src/wiki/StrictMode">Strict mode PRAGMA</a>
     * <p>
     * By default, sqlite foreign key constrains are off,
     * I have turned them on to catch insert errors that may arise
     * <a href="https://sqlite.org/foreignkeys.html">Foreign Key PRAGMA</a>
     * <p>
     * Synchronous PRAGMA determines how often the database writes to the disk, with higher levels
     * of synchronous mode writing more often with, this comes with the trade of slower operations at higher levels
     * <a href="https://sqlite.org/pragma.html#pragma_synchronous">Synchronous PRAGMA</a>
     * <p>
     * Locking mode by default allows multiple processes to access the database file,
     * however I have set it to exclusive to prevent two or more instances from reading or writing
     * to prevent data corruption in user space.
     * <a href="https://sqlite.org/pragma.html#pragma_locking_mode">Locking mode PRAGMA</a>
     * <p>
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     * @param instance (sqlite) database connection
     */
    private static void setDataBasePragma(Connection instance) {
        logger.debug("Setting database pragma");
        try {
            Statement stmt = instance.createStatement();
            stmt.executeUpdate("PRAGMA strict = ON;");
            stmt.executeUpdate( "PRAGMA foreign_keys = ON;");
            stmt.executeUpdate("PRAGMA synchronous = FULL;");
            stmt.executeUpdate("PRAGMA locking_mode = EXCLUSIVE;");
            stmt.close();
            logger.debug("database pragma set successfully.");
        }catch (SQLException e){
            logger.fatal(e.getMessage());
        }
    }

    /**
     * This function checks the database to see if the desired PRAGMAAs have been set
     * <p>
     * The foreign key pragma can return 1 (true) for enabled or 0 (false) for disabled
     * <p>
     * The synchronous pragma can return 0 (OFF), 1 (NORMAL), 2 (FULL) or 3 (EXTRA)
     * <p>
     *  The locking mode pragma can return either NORMAL or EXCLUSIVE
     * <p>
     * @see com.cab302.cab302project.model.SqliteConnection#setDataBasePragma
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     * @param instance (sqlite) database connection
     */
    private static void checkPragma(Connection instance) {
        try {
            Statement stmt = instance.createStatement();
            ResultSet rsForeignKeys = stmt.executeQuery("PRAGMA foreign_keys;");
            ResultSet rsSynchronous = stmt.executeQuery("PRAGMA synchronous;");
            ResultSet rsLockingMode = stmt.executeQuery("PRAGMA locking_mode;");
            if (rsForeignKeys.next()) {
                int foreignKeysEnabled = rsForeignKeys.getInt(1);
                logger.debug("Foreign keys enabled: {}", foreignKeysEnabled);
            }
            if (rsSynchronous.next()) {
                int synchronousMode = rsSynchronous.getInt(1);
                logger.info("Synchronous Mode: {}", synchronousMode);
            }
            if (rsLockingMode.next()) {
                String lockingMode = rsLockingMode.getString(1);
                logger.info("Locking Mode: {}", lockingMode);
            }
            rsForeignKeys.close();
            rsSynchronous.close();
            rsLockingMode.close();
            stmt.close();
        } catch (Exception e){
            logger.fatal(e.getMessage());
        }
    }
}
