package com.cab302.cab302project.model.user;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("Andrew", "Clarke", "legend@qut.edu.au", "VeryStrongPassword");
    }

    @Test
    void getId() {
        assertEquals(0, user.getId());

    }

    @Test
    void setId() {
        assertEquals(0, user.getId());
        user.setId(1);
        assertEquals(1, user.getId());
    }

    @Test
    void getFirstName() {
        assertEquals("Andrew", user.getFirstName());
    }

    @Test
    void setFirstName() {
        user.setFirstName("Andy");
        assertEquals("Andy", user.getFirstName());
    }

    @Test
    void getLastName() {
        assertEquals("Clarke", user.getLastName());
    }

    @Test
    void setLastName() {
        user.setLastName("Test");
        assertEquals("Test", user.getLastName());
    }

    @Test
    void getEmail() {
        assertEquals("legend@qut.edu.au", user.getEmail());
    }

    @Test
    void setEmail() {
        user.setEmail("David@gmail.com");
        assertEquals("David@gmail.com", user.getEmail());
    }

    @Test
    void getPassword() {
        assertEquals("VeryStrongPassword", user.getPassword());
    }

    @Test
    void setPassword() {
        user.setPassword("Open Sesame");
        assertEquals("Open Sesame", user.getPassword());
    }
}