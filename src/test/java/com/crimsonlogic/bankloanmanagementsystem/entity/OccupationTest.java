package com.crimsonlogic.bankloanmanagementsystem.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OccupationTest {

    private Occupation occupation;

    @BeforeEach
    void setUp() {
        // Initialize an Occupation object before each test case
        occupation = new Occupation();
    }

    @Test
    void testGenerateId() {
        // Verify that the ID is generated before persisting
        occupation.generateId();
        assertNotNull(occupation.getOccupationId());
        assertTrue(occupation.getOccupationId().startsWith("OCC"));
    }

    @Test
    void testOccupationConstructor() {
        // Create an instance of Occupation using the constructor
        Occupation occupation = new Occupation();
        occupation.setOccupationType("Engineer");
        occupation.setOccupationName("Software Engineer");
        occupation.setCompanyName("Tech Corp");
        occupation.setSalary(75000.0);
        
        // Validate the properties of the occupation object
        assertEquals("Engineer", occupation.getOccupationType());
        assertEquals("Software Engineer", occupation.getOccupationName());
        assertEquals("Tech Corp", occupation.getCompanyName());
        assertEquals(75000.0, occupation.getSalary());
    }

    @Test
    void testSettersAndGetters() {
        // Set values using setters
        occupation.setOccupationType("Doctor");
        occupation.setOccupationName("Surgeon");
        occupation.setCompanyName("Health Inc");
        occupation.setSalary(120000.0);
        
        // Validate values using getters
        assertEquals("Doctor", occupation.getOccupationType());
        assertEquals("Surgeon", occupation.getOccupationName());
        assertEquals("Health Inc", occupation.getCompanyName());
        assertEquals(120000.0, occupation.getSalary());
    }

   
}
