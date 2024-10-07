package com.crimsonlogic.bankloanmanagementsystem.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginTest {

    private Login login;

    @BeforeEach
    void setUp() {
        // Initialize a Login object before each test case
        login = new Login();
    }

    @Test
    void testGenerateId() {
        // Verify that the ID is generated before persisting
        login.generateId();
        assertNotNull(login.getLoginId());
        assertTrue(login.getLoginId().startsWith("LOG"));
    }

    @Test
    void testLoginConstructor() {
        // Create an instance of Login using setters
        login.setEmail("test@example.com");
        login.setPassword("securePassword");
        
        // Validate the properties of the login object
        assertEquals("test@example.com", login.getEmail());
        assertEquals("securePassword", login.getPassword());
    }

    @Test
    void testSettersAndGetters() {
        // Set values using setters
        login.setEmail("user@example.com");
        login.setPassword("password123");
        
        // Validate values using getters
        assertEquals("user@example.com", login.getEmail());
        assertEquals("password123", login.getPassword());
    }

    @Test
    void testRoleAssociation() {
        // Create a Role object and set it in the Login object
        Role role = new Role();
        role.setRoleName("User");
        login.setRole(role);
        
        // Validate the role association
        assertNotNull(login.getRole());
        assertEquals("User", login.getRole().getRoleName());
    }

   
}
