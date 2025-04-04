package com.cab302.cab302project.model.user;

import com.cab302.cab302project.model.SqliteConnection;

import java.sql.Connection;

public class SqliteUserDAO {

    private final Connection con;

    public SqliteUserDAO() {
        con = SqliteConnection.getInstance();
    }

    private final String createUserTableSQL = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                firstName TEXT NOT NULL,
                lastName TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL
            );""";
    private final String addUserSQL = """
            INSERT INTO users (firstName, lastName, email, password)
                VALUES (?, ?, ?, ?);
            """;
    private final String updateUserSQL = """
            UPDATE users SET firstName = ?, lastName = ?, email = ?, password = ? 
                WHERE id = ?;
            """;
    private final String getUserByIdSQL = """
            SELECT * FROM users WHERE id = ?;
            """;
    private final String getUserByEmailSQL = """
            SELECT * FROM users WHERE email = ?;
            """;
}
