package com.cab302.cab302project.model;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteContactDAOTestHelper {

        // Helper method to get the contact count from the database
        public static int getContactCount(Connection con) {
            int count = 0;
            try {
                Statement statement = con.createStatement();
                String sql = "SELECT COUNT(*) FROM contacts";
                ResultSet resultSet = statement.executeQuery(sql);
                resultSet.next();
                count = resultSet.getInt(1);
            } catch (SQLException e) {
                throw new RuntimeException("Error getting contact count", e);
            }
            return count;
        }

        public static void deleteAllContacts(Connection con) {
            try {
                Statement statement = con.createStatement();
                String sql = "DELETE FROM contacts";
                statement.executeUpdate(sql);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static void dropTable(Connection con) {
            try{
                Statement statement = con.createStatement();
                String sql = "DROP TABLE IF EXISTS contacts";
                statement.executeUpdate(sql);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static void closeConnection(Connection con) {
            try {
                con.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static void AddNUsers(IContactDAO con, int numberOfUsers) {
            try {
                for (int i = 0; i < numberOfUsers; i++) {
                    con.addContact(new Contact("test-user-"+i, "tester", "tester-"+i+"@tester.com", "tester@tester.com"));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
}
