package com.cab302.cab302project.model;

import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MockContactDAOTest {


    public static MockContactDAO mockContactDAO;

    @BeforeAll
    static void beforeAll() {
        mockContactDAO = new MockContactDAO();
    }

    @AfterEach
    void afterEach() {
        mockContactDAO.resetMockDatabase();
    }

    @Test
    void addContact() {
        assertEquals(3, mockContactDAO.contacts.size());
        mockContactDAO.addContact(new Contact("test", "tester", "test@test.com", "123456789"));
        assertEquals(4, mockContactDAO.contacts.size());
    }

    @Test
    void getContact() {
        Contact contact = mockContactDAO.getContact(1);
        assertEquals("John", contact.getFirstName());
        assertEquals("Doe", contact.getLastName());
        assertNotEquals("janedoe@example.com", contact.getEmail());

        Contact contact2 = mockContactDAO.getContact(2);
        assertEquals("Jane", contact2.getFirstName());
        assertEquals("Doe", contact2.getLastName());
        assertEquals("janedoe@example.com", contact2.getEmail());
    }


    @Test
    void updateContact() {
        Contact contact = mockContactDAO.getContact(1);
        assertEquals("John", contact.getFirstName());
        assertEquals("Doe", contact.getLastName());

        contact.setFirstName("Test");
        contact.setLastName("Tester");
        mockContactDAO.updateContact(contact);

        Contact contact2 = mockContactDAO.getContact(1);
        assertNotEquals("John", contact2.getFirstName());
        assertEquals("Test", contact2.getFirstName());
    }

    @Test
    void deleteContact() {
        assertEquals(3, mockContactDAO.contacts.size());
        Contact contact = mockContactDAO.getContact(1);
        mockContactDAO.deleteContact(contact);
        assertEquals(2, mockContactDAO.contacts.size());
    }


    @Test
    void getAllContacts() {
        List<Contact> contacts = mockContactDAO.getAllContacts();
        assertEquals(3, contacts.size());
    }
}