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

    private final String updateUserSQL = "UPDATE user SET first_name = ?, last_name = ?, email = ?, password = ? WHERE id = ?";
    private final String getUserByIdSQL = "SELECT * FROM user WHERE id = ?";
    private final String getUserByEmailSQL = "SELECT * FROM user WHERE email = ?";

    @Override
    public void addUser (User user) {
        try {
            con.setAutoCommit(false);
            String addUserSQL = "INSERT INTO user (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
            try (PreparedStatement sql = con.prepareStatement(addUserSQL)) {
                sql.setString(1, user.getFirstName());
                sql.setString(2, user.getLastName());
                sql.setString(3, user.getEmail());
                sql.setString(4, user.getPassword());
                sql.executeUpdate();
                con.commit();
                logger.error("Add user ransaction completed successfully.");
            } catch (SQLException e) {
                con.rollback();
                logger.error("Add user transaction failed.");
                logger.fatal(e.getMessage());
            }finally {
                con.setAutoCommit(true);
            }
        }catch (Exception e) {
            logger.fatal(e.getMessage());
        }
    }

    @Override
    public void updateUser (User user) {
        try {
            con.setAutoCommit(false);
            try (PreparedStatement sql = con.prepareStatement(updateUserSQL)) {
                sql.setString(1, user.getFirstName());
                sql.setString(2, user.getLastName());
                sql.setString(3, user.getEmail());
                sql.setString(4, user.getPassword());
                sql.setInt(5, user.getId());
                sql.executeUpdate();
                con.commit();
                logger.error("Update user transaction completed successfully.");
            } catch (SQLException e) {
                con.rollback();
                logger.error("Update user transaction failed.");
                logger.fatal(e.getMessage());
            }finally {
                con.setAutoCommit(true);
            }
        }catch (Exception e) {
            logger.fatal(e.getMessage());
        }
    }

    @Override
    public User getUser (int id) {
        User user = null;
        try {
            con.setAutoCommit(false);
            try (PreparedStatement sql = con.prepareStatement(getUserByIdSQL)) {
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
            }catch (SQLException e) {
                con.rollback();
                logger.error("Get user by id transaction failed.");
                logger.fatal(e.getMessage());
            }finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.fatal(e.getMessage());
        }
        return user;
    }

    @Override
    public User getUser(String email) {
        User user = null;
        try {
            con.setAutoCommit(false);
            try (PreparedStatement sql = con.prepareStatement(getUserByEmailSQL)) {
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
            }catch (SQLException e) {
                con.rollback();
                logger.error("Get user by email transaction failed.");
                logger.fatal(e.getMessage());
            }finally {
                con.setAutoCommit(true);
            }

        } catch (Exception e) {
            logger.fatal(e.getMessage());
        }
        return user;
    }
}
