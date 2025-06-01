package com.cab302.cab302project.model.user;

/**
 * Represents a user of the application.
 * Each User has an id, first name, last name, email, and password.
 * This class provides getter and setter methods for user data.
 *
 * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
 */
public final class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    /**
     * Constructs a new User with the specified first name, last name, email, and password.
     *
     * @param firstName the user's first name
     * @param lastName  the user's last name
     * @param email     the user's email address
     * @param password  the user's password
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    /**
     * Gets the user's ID.
     *
     * @return the user's ID
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the user's first name.
     *
     * @return the user's first name
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the user's last name.
     *
     * @return the user's last name
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the user's email address.
     *
     * @return the user's email
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the user's password.
     *
     * @return the user's password
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's ID.
     * Intended for internal use only.
     *
     * @param id the user's ID to set
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the user's first name.
     *
     * @param firstName the new first name to set
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the user's last name.
     *
     * @param lastName the new last name to set
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the user's email address.
     *
     * @param email the new email to set
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the user's password.
     *
     * @param password the new password to set
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
