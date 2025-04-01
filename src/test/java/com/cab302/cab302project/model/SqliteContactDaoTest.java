package com.cab302.cab302project.model;

import org.junit.jupiter.api.*;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SqliteContactDaoTest {

    private static IContactDAO contactDAO;

    private static Connection con;

    @BeforeAll
    static void setUp() {
        SqliteConnection.setTestingModeTrue();
        contactDAO = new SqliteContactDao();
        con = SqliteConnection.getInstance();
    }

    @AfterAll
    static void tearDown() throws SQLException {
        SqliteContactsDaoTestHelper.deleteAllContacts(con);
    }

    @BeforeEach
    void pause() throws InterruptedException {
        Thread.sleep(50);
    }

    @Test
    @Order(1)
    void addContact() {
        contactDAO.addContact(new Contact("Andrew", "TheGoat", "thegoat@qut.edu.au", "1300655506"));
        assertEquals(1,  SqliteContactsDaoTestHelper.getContactCount(con));
    }

    @Test
    @Order(2)
    void getContact(){
        Contact contact = contactDAO.getContact(1);
        assertEquals("Andrew", contact.getFirstName());
        assertEquals("TheGoat", contact.getLastName());
        assertEquals("thegoat@qut.edu.au", contact.getEmail());
        assertEquals("1300655506", contact.getPhone());
        assertEquals(1, contact.getId());
        assertEquals("Andrew TheGoat", contact.getFullName());
    }


    @Test
    @Order(3)
    void updateContact(){
        Contact contact = contactDAO.getContact(1);
        contact.setFirstName("Andrew");
        contact.setLastName("TheGoat");
        contact.setEmail("thegoat@everything.com");
        contactDAO.updateContact(contact);
        Contact updatedContact = contactDAO.getContact(1);
        assertEquals("Andrew", updatedContact.getFirstName());
        assertEquals("TheGoat", updatedContact.getLastName());
        assertEquals("thegoat@everything.com", updatedContact.getEmail());
    }

    @Test
    @Order(4)
    void deleteContact() {
        Contact contact = contactDAO.getContact(1);
        contactDAO.deleteContact(contact);
        assertEquals(0, SqliteContactsDaoTestHelper.getContactCount(con));
    }


    @Test
    @Order(5)
    void getAllContacts() {
        SqliteContactsDaoTestHelper.AddNUsers(contactDAO, 10);
        List<Contact> contacts = contactDAO.getAllContacts();
        assertEquals(10, contacts.size());

        SqliteContactsDaoTestHelper.deleteAllContacts(con);
        SqliteContactsDaoTestHelper.AddNUsers(contactDAO, 50);
        List<Contact> contactsTestTwo = contactDAO.getAllContacts();
        assertEquals(50, contactsTestTwo.size());


    }
}