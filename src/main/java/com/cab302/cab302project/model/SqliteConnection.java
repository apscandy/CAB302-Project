package com.cab302.cab302project.model;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteConnection {
    private static Connection instance = null;

    private SqliteConnection(String url)  {

        try{
            instance = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Connection getInstance(Boolean testMode) {
        if (instance == null) {
            if (testMode == false) {
                new SqliteConnection("jdbc:sqlite:contacts.db");
            }else{
                new SqliteConnection("jdbc:sqlite:test_contacts.db");
            }

        }
        return instance;
    }
}
