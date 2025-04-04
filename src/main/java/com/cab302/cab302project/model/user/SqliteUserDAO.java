package com.cab302.cab302project.model.user;

import com.cab302.cab302project.model.SqliteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SqliteUserDAO implements IUserDAO {

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

    public void  createUserTable() {
        try {
            PreparedStatement sql = con.prepareStatement(createUserTableSQL);
            sql.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addUser (User user) {
        try {
            PreparedStatement sql = con.prepareStatement(addUserSQL);
            sql.setString(1, user.getFirstName());
            sql.setString(2, user.getLastName());
            sql.setString(3, user.getEmail());
            sql.setString(4, user.getPassword());
            sql.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser (User user) {
        try {
            PreparedStatement sql = con.prepareStatement(updateUserSQL);
            sql.setString(1, user.getFirstName());
            sql.setString(2, user.getLastName());
            sql.setString(3, user.getEmail());
            sql.setString(4, user.getPassword());
            sql.setInt(5, user.getId());
            sql.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUser (int id) {
        User user = null;
        try {
            PreparedStatement sql = con.prepareStatement(getUserByIdSQL);
            sql.setInt(1, id);
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                user = new User (
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("email"),
                        result.getString("password")
                );
                user.setId(result.getInt("id"));
            }
            result.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User getUser(String email) {
        User user = null;
        try {
            PreparedStatement sql = con.prepareStatement(getUserByEmailSQL);
            sql.setString(1, email);
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                user = new User (
                        result.getString("firstName"),
                        result.getString("lastName"),
                        result.getString("email"),
                        result.getString("password")

                );
                user.setId(result.getInt("id"));
            }
            result.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
