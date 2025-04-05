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
        createDeckTable();
    }

    private void createUserTable() {
        try{
            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS user ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "first_name TEXT NOT NULL,"
                    + "last_name TEXT NOT NULL,"
                    + "email TEXT NOT NULL,"
                    + "password TEXT NOT NULL"
                    + ")";
            stmt.executeUpdate(sql);
        }catch (Exception e){
            logger.fatal("Error creating user table: {}", e.getMessage());
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

    public void createSecurityQuestionTable() {
        try {
            Statement stmt = con.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS securityQuestions ("
                    + "id INTEGER PRIMARY KEY,"
                    + "secQuestionOne TEXT NOT NULL,"
                    + "secAnswerOne TEXT NOT NULL,"
                    + "secQuestionTwo TEXT NOT NULL,"
                    + "secAnswerTwo TEXT NOT NULL,"
                    + "secQuestionThree TEXT NOT NULL,"
                    + "secAnswerThree TEXT NOT NULL,"
                    + "FOREIGN KEY(id) REFERENCES users(id)"
                    + ")";
            stmt.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
