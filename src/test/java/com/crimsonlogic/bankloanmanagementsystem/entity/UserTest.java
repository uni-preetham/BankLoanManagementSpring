package com.crimsonlogic.bankloanmanagementsystem.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.crimsonlogic.bankloanmanagementsystem.util.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testGenerateId() {
        // Verify that the ID is generated before persisting
        user.generateId();
        assertNotNull(user.getUserId());
        assertTrue(user.getUserId().startsWith("USER"));
    }

    @Test
    void testUserDetails() {
        // Set user details
        user.setFirstName("Preetham");
        user.setLastName("A A");
        user.setPhone("8653803523");
        user.setCreditScore(700);

        // Validate user details
        assertEquals("Preetham", user.getFirstName());
        assertEquals("A A", user.getLastName());
        assertEquals("8653803523", user.getPhone());
        assertEquals(700, user.getCreditScore());
    }

    @Test
    void testToString() {
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhone("1234567890");
        user.setCreditScore(700);

        String expectedString = "User{" +
                "userId='null', " +  // ID will be null in this context as we are not persisting
                "firstName='John', " +
                "lastName='Doe', " +
                "phone='1234567890', " +
                "creditScore=700" +
                '}';
        
        assertEquals(expectedString, user.toString());
    }
}
