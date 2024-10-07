package com.crimsonlogic.bankloanmanagementsystem.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ManagerTest {

    private Manager manager;

    @BeforeEach
    void setUp() {
        // Initialize a Manager object before each test case
        manager = new Manager();
    }

    @Test
    void testGenerateId() {
        // Verify that the ID is generated before persisting
        manager.generateId();
        assertNotNull(manager.getManagerId());
        assertTrue(manager.getManagerId().startsWith("MAN"));
    }

    @Test
    void testManagerConstructor() {
        // Create an instance of Manager using setters
        manager.setFirstName("John");
        manager.setLastName("Doe");
        manager.setEmail("john.doe@example.com");
        manager.setPhone("1234567890");
        
        // Validate the properties of the manager object
        assertEquals("John", manager.getFirstName());
        assertEquals("Doe", manager.getLastName());
        assertEquals("john.doe@example.com", manager.getEmail());
        assertEquals("1234567890", manager.getPhone());
    }

    @Test
    void testSettersAndGetters() {
        // Set values using setters
        manager.setFirstName("Jane");
        manager.setLastName("Smith");
        manager.setEmail("jane.smith@example.com");
        manager.setPhone("0987654321");
        
        // Validate values using getters
        assertEquals("Jane", manager.getFirstName());
        assertEquals("Smith", manager.getLastName());
        assertEquals("jane.smith@example.com", manager.getEmail());
        assertEquals("0987654321", manager.getPhone());
    }

   
}
