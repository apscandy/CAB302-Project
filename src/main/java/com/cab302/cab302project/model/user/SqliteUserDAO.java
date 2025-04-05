package com.cab302.cab302project.model.user;

import com.cab302.cab302project.model.SqliteConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqliteUserDAO implements IUserDAO {

    private static final Logger logger = LogManager.getLogger(SqliteUserDAO.class);

    private final Connection con;

    public SqliteUserDAO() {
        con = SqliteConnection.getInstance();
    }

    private final  String addUserSQL = "INSERT INTO user (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
    private final String updateUserSQL = "UPDATE user SET first_name = ?, last_name = ?, email = ?, password = ? WHERE id = ?";
    private final String getUserByIdSQL = "SELECT * FROM user WHERE id = ?";
    private final String getUserByEmailSQL = "SELECT * FROM user WHERE email = ?";

    @Override
    public void addUser (User user) {
        try (PreparedStatement sql = con.prepareStatement(addUserSQL)) {
            sql.setString(1, user.getFirstName());
            sql.setString(2, user.getLastName());
            sql.setString(3, user.getEmail());
            sql.setString(4, user.getPassword());
            sql.executeUpdate();
            ResultSet rs = sql.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
            }
            rs.close();
            sql.close();
            logger.info("Add user transaction completed successfully.");
        }catch (SQLException sqlException) {
            logger.fatal("Add user transaction failed: {}", sqlException.getMessage());
        }catch (Exception e) {
            logger.fatal("Something went wrong: {}", e.getMessage());
        }
    }

    @Override
    public void updateUser (User user) {
        try (PreparedStatement sql = con.prepareStatement(updateUserSQL)) {
            sql.setString(1, user.getFirstName());
            sql.setString(2, user.getLastName());
            sql.setString(3, user.getEmail());
            sql.setString(4, user.getPassword());
            sql.setInt(5, user.getId());
            sql.executeUpdate();
            sql.close();
            logger.info("Update user transaction completed successfully.");
        }catch (SQLException sqlException) {
            logger.fatal("Update user transaction failed: {}", sqlException.getMessage());
        }catch (Exception e) {
            logger.fatal("Something went wrong: {}", e.getMessage());
        }
    }

    @Override
    public User getUser (int id) {
        User user = null;
        try (PreparedStatement sql = con.prepareStatement(getUserByIdSQL)) {
            sql.setInt(1, id);
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                String firstName = result.getString("first_name");
                String lastName = result.getString("last_name");
                String email = result.getString("email");
                String password = result.getString("password");
                user = new User(firstName, lastName, email, password);
                user.setId(result.getInt("id"));
            }
            sql.close();
            result.close();
            logger.info("Get user transaction completed successfully.");
       }catch (SQLException sqlException) {
            logger.fatal("Get user by id transaction failed: {}", sqlException.getMessage());
        }catch (Exception e) {
            logger.fatal("Something went wrong: {}", e.getMessage());
        }
        return user;
    }

    @Override
    public User getUser(String email) {
        User user = null;
        try (PreparedStatement sql = con.prepareStatement(getUserByEmailSQL)) {
            sql.setString(1, email);
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                String firstName = result.getString("first_name");
                String lastName = result.getString("last_name");
                String emails = result.getString("email");
                String password = result.getString("password");
                user = new User(firstName, lastName, emails, password);
                user.setId(result.getInt("id"));
            }
            sql.close();
            result.close();
            logger.info("Get user transaction completed successfully.");
        }catch (SQLException sqlException) {
            logger.fatal("Get user by email transaction failed: {}", sqlException.getMessage());
        }catch (Exception e) {
            logger.fatal("Something went wrong: {}", e.getMessage());
        }
        return user;
    }
}
