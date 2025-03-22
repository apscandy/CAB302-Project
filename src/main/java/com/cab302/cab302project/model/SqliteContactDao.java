package com.cab302.cab302project.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteContactDao implements IContactDAO{

    private Connection con;


    public SqliteContactDao() {
        con = SqliteConnection.getInstance();
        createTable();
    }

    private void createTable() {
        try{
            Statement statement = con.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS contacts ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "firstName VARCHAR NOT NULL,"
                    + "lastName VARCHAR NOT NULL,"
                    + "phone VARCHAR NOT NULL,"
                    + "email VARCHAR NOT NULL"
                    + ")";
            statement.execute(query);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertSampleData() {
        try {
            Statement clearStatement = con.createStatement();
            String clearQuery = "DELETE FROM contacts";
            clearStatement.execute(clearQuery);
            Statement insertStatement = con.createStatement();
            String insertQuery = "INSERT INTO contacts (firstName, lastName, phone, email) VALUES "
                    + "('John', 'Doe', '0423423423', 'johndoe@example.com'),"
                    + "('Jane', 'Doe', '0423423424', 'janedoe@example.com'),"
                    + "('Jay', 'Doe', '0423423425', 'jaydoe@example.com')";
            insertStatement.execute(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void addContact(Contact contact) {
        try {
            String query = "INSERT INTO contacts (firstName, lastName, phone, email) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, contact.getFirstName());
            preparedStatement.setString(2, contact.getLastName());
            preparedStatement.setString(3, contact.getPhone());
            preparedStatement.setString(4, contact.getEmail());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                contact.setId(resultSet.getInt(1));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateContact(Contact contact) {
        try {
            String query = "UPDATE contacts SET firstName = ?, lastName = ?, phone = ?, email = ? WHERE id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, contact.getFirstName());
            preparedStatement.setString(2, contact.getLastName());
            preparedStatement.setString(3, contact.getPhone());
            preparedStatement.setString(4, contact.getEmail());
            preparedStatement.setInt(5, contact.getId());
            preparedStatement.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteContact(Contact contact) {
        try {
            String query = "DELETE FROM contacts WHERE id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, contact.getId());
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public Contact getContact(int id) {
        try{
            PreparedStatement statement = con.prepareStatement("SELECT * FROM contacts WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                Contact contact = new Contact(firstName, lastName, phone, email);
                contact.setId(id);
                return contact;
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();

        try{
            Statement statement = con.createStatement();
            String query = "SELECT * FROM contacts";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                Contact contact = new Contact(firstName, lastName, phone, email);
                contact.setId(id);
                contacts.add(contact);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }
}
