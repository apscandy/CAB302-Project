package com.cab302.cab302project.model.user;

import com.cab302.cab302project.model.SqliteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqliteUserDAO implements IUserDAO {

    private final Connection con;

    public SqliteUserDAO() {
        con = SqliteConnection.getInstance();
    }

    private final String addUserSQL = """
            INSERT INTO user (first_name, last_name, email, password)
                VALUES (?, ?, ?, ?);
            """;
    private final String updateUserSQL = """
            UPDATE user SET first_name = ?, last_name = ?, email = ?, password = ? 
                WHERE id = ?;
            """;
    private final String getUserByIdSQL = """
            SELECT * FROM user WHERE id = ?;
            """;
    private final String getUserByEmailSQL = """
            SELECT * FROM user WHERE email = ?;
            """;

    @Override
    public void addUser (User user) {
        try {
            try {
                con.setAutoCommit(false);
                PreparedStatement sql = con.prepareStatement(addUserSQL);
                sql.setString(1, user.getFirstName());
                sql.setString(2, user.getLastName());
                sql.setString(3, user.getEmail());
                sql.setString(4, user.getPassword());
                sql.executeUpdate();
                ResultSet result = sql.getGeneratedKeys();
                if (result.next()) {
                    user.setId(result.getInt(1));
                }
                con.commit();
                sql.close();
                result.close();
            } catch (SQLException e) {
                con.rollback();
                e.getMessage();
            } finally {
                con.setAutoCommit(true);
            }
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
                        result.getString("first_name"),
                        result.getString("last_name"),
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
                        result.getString("first_name"),
                        result.getString("last_name"),
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
