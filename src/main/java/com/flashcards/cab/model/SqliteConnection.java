package com.flashcards.cab.model;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteConnection {
    private static Connection instance = null;
    private static Boolean testingMode = false;

    private SqliteConnection()  {
        String url;
        if (testingMode) {
            url = "jdbc:sqlite::memory:";
        }else{
            url = "jdbc:sqlite:flashCards.db";
        }
        try{
            instance = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Connection getInstance() {
        if (instance == null) {
            new SqliteConnection();
        }
        return instance;
    }

    public static void setTestingModeTrue() {
        testingMode = true;
    }
}
