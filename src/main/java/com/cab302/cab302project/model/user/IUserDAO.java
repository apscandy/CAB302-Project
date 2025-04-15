package com.cab302.cab302project.model.user;

/**
 * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
 */
public interface IUserDAO {
    void addUser(User user);
    void updateUser(User user);
    User getUser(int id);
    User getUser(String email);
}