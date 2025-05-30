package com.cab302.cab302project.model;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;


/**
 * Manages SQLite database connections for the application.
 * Provides both in-memory and persistent database support.
 * <p>
 * This class implements the singleton pattern to ensure only one connection is created.
 * It allows testing with an ephemeral in-memory database by calling setTestingModeTrue().
 *
 * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
 */
public final class SqliteConnection {
    private static final Logger logger = LogManager.getLogger(SqliteConnection.class);
    private static Connection instance = null;
    private static Boolean testingMode = false;


    /**
     * Private constructor to initialize the SQLite database connection.
     * Sets up the connection based on the testing mode flag.
     * <p>
     * If testing mode is enabled, an in-memory database is used; otherwise,
     * a persistent file-based database is created at 'flashCards.db'.
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
            setDataBasePragma(instance);
            checkPragma(instance);
            logger.info("Database connection established.");
        } catch (SQLException e) {
            logger.fatal(e.getMessage());
        }
    }


    /**
     * Returns the singleton instance of the SQLite database connection.
     * <p>
     * If no instance exists, it creates one using the private constructor.
     * This method is not thread-safe. Ensure proper synchronization if used in concurrent contexts.
     *
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     * @return The singleton SQLite database connection.
     */
    public static Connection getInstance() {
        if (instance == null) {
            new SqliteConnection();
        }
        return instance;
    }

    /**
     * Enables testing mode to use an in-memory SQLite database.
     * <p>
     * When enabled, all database operations are performed in memory,
     * and data is not persisted between runs. This ensures ephemeral test environments.
     * <p>
     * Must be called before {@link #getInstance()} to take effect.
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
     * Verifies that the SQLite database has been configured with the expected PRAGMA settings.
     * <p>
     * Checks foreign key status, synchronous mode, and locking mode to ensure they match
     * the configurations set by {@link #setDataBasePragma}.
     * <p>
     * The foreign key pragma can return 1 (true) for enabled or 0 (false) for disabled
     * <p>
     * The synchronous pragma can return 0 (OFF), 1 (NORMAL), 2 (FULL) or 3 (EXTRA)
     * <p>
     *  The locking mode pragma can return either NORMAL or EXCLUSIVE
     * <p>
     * @author Andrew Clarke (a40.clarke@connect.qut.edu.au)
     * @param instance (sqlite) database connection
     */
    private static void checkPragma(Connection instance) {
        try {
            Statement stmt = instance.createStatement();

            ResultSet rsForeignKeys = stmt.executeQuery("PRAGMA foreign_keys;");
            if (rsForeignKeys.next()) {
                int foreignKeysEnabled = rsForeignKeys.getInt(1);
                logger.info("Foreign keys enabled: {}", foreignKeysEnabled);
            }
            rsForeignKeys.close();

            ResultSet rsSynchronous = stmt.executeQuery("PRAGMA synchronous;");
            if (rsSynchronous.next()) {
                int synchronousMode = rsSynchronous.getInt(1);
                logger.info("Synchronous Mode: {}", synchronousMode);
            }
            rsSynchronous.close();

            ResultSet rsLockingMode = stmt.executeQuery("PRAGMA locking_mode;");
            if (rsLockingMode.next()) {
                String lockingMode = rsLockingMode.getString(1);
                logger.info("Locking Mode: {}", lockingMode);
            }
            rsLockingMode.close();
            stmt.close();
        } catch (Exception e){
            logger.fatal(e.getMessage());
        }
    }
}


