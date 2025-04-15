package com.cab302.cab302project.model.user;

import com.cab302.cab302project.error.model.user.FailedToCreateUserException;
import com.cab302.cab302project.error.model.user.FailedToGetUserException;
import com.cab302.cab302project.error.model.user.FailedToUpdateUserException;
import com.cab302.cab302project.model.SqliteConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
 */
public final class SqliteUserDAO implements IUserDAO {

    private final Connection con;
    private static final Logger logger = LogManager.getLogger(SqliteUserDAO.class);

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    public SqliteUserDAO() {
        con = SqliteConnection.getInstance();
    }

    private final String addUserSQL = "INSERT INTO user (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
    private final String updateUserSQL = "UPDATE user SET first_name = ?, last_name = ?, email = ?, password = ? WHERE id = ?";
    private final String getUserByIdSQL = "SELECT * FROM user WHERE id = ?";
    private final String getUserByEmailSQL = "SELECT * FROM user WHERE email = ?";

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Override
    public void addUser (User user) {
        if (user == null || user.getFirstName() == null || user.getLastName() == null || user.getEmail() == null) {
            throw new IllegalArgumentException();
        }
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
                logger.error(e.getMessage());
                throw new FailedToCreateUserException(e.getMessage());
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToCreateUserException(e.getMessage());
        }
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Override
    public void updateUser (User user) {
        try {
            con.setAutoCommit(false);
            try {
                PreparedStatement sql = con.prepareStatement(updateUserSQL);
                sql.setString(1, user.getFirstName());
                sql.setString(2, user.getLastName());
                sql.setString(3, user.getEmail());
                sql.setString(4, user.getPassword());
                sql.setInt(5, user.getId());
                sql.executeUpdate();
                con.commit();
                sql.close();
            } catch (SQLException e) {
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToUpdateUserException(e.getMessage());
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToUpdateUserException(e.getMessage());
        }
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Override
    public User getUser (int id) {
        User user = null;
        try {
            con.setAutoCommit(false);
            try (PreparedStatement sql = con.prepareStatement(getUserByIdSQL)) {
                sql.setInt(1, id);
                ResultSet result = sql.executeQuery();
                if (result.next()) {
                    String firstName = result.getString("first_name");
                    String lastName = result.getString("last_name");
                    String password = result.getString("password");
                    String emails = result.getString("email");
                    int ids = result.getInt("id");
                    user = new User(firstName, lastName, emails, password);
                    user.setId(ids);
                }
                con.commit();
                sql.close();
                result.close();
           }catch (SQLException e) {
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToGetUserException(e.getMessage());
            }finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToGetUserException(e.getMessage());
        }
        return user;
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Override
    public User getUser(String email) {
        User user = null;
        try {
            con.setAutoCommit(false);
            try (PreparedStatement sql = con.prepareStatement(getUserByEmailSQL)) {
                sql.setString(1, email);
                ResultSet result = sql.executeQuery();
                if (result.next()) {
                    String firstName = result.getString("first_name");
                    String lastName = result.getString("last_name");
                    String password = result.getString("password");
                    String emails = result.getString("email");
                    int id = result.getInt("id");
                    user = new User(firstName, lastName, emails, password);
                    user.setId(id);
                }
                con.commit();
                sql.close();
                result.close();
            }catch (SQLException e) {
                con.rollback();
                logger.error(e.getMessage());
                throw new FailedToGetUserException(e.getMessage());
            }finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new FailedToGetUserException(e.getMessage());
        }
        return user;
    }
}
