package com.cab302.cab302project.model.user;

import java.util.ArrayList;
import java.util.List;

public class MockUserDAO implements IUserDAO {
    private static int autoIncrementedId  = 1;
    private static final List<User> users = new ArrayList<>();

    public MockUserDAO() {
        addUser(new User("John", "Doe", "johndoe@example.com", "VerySecure"));
    }

    @Override
    public void addUser(User user) {
        user.setId(autoIncrementedId++);
        users.add(user);
    }

    @Override
    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == user.getId()) {
                users.set(i, user);
                break;
            }
        }
    }

    @Override
    public User getUser(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User getUser(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

}
