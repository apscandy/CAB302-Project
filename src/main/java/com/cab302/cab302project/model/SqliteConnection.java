package com.cab302.cab302project.model;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
