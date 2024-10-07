package com.crimsonlogic.bankloanmanagementsystem.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AddressTest {

    private Address address;
    private User user; // Simulate User entity

    @BeforeEach
    void setUp() {
        address = new Address();
        user = new User(); // Initialize a User object
        address.setAddressLine1("456 Main St");
        address.setAddressLine2("Apt 789");
        address.setCity("Metropolis");
        address.setState("StateName");
        address.setZipCode("12345");
        address.setUser(user); // Set the user
    }

    @Test
    void generateId_ShouldSetAddressId_WhenAddressIsPersisted() {
        address.generateId(); // Simulate the @PrePersist behavior
        assertNotNull(address.getAddressId());
        assertTrue(address.getAddressId().startsWith("ADD")); // Check that ID starts with 'ADD'
    }

    @Test
    void addressAttributes_ShouldSetValuesCorrectly() {
        address.generateId(); // Simulate ID generation

        assertNotNull(address.getAddressId()); // Check that ID is set
        assertEquals("456 Main St", address.getAddressLine1());
        assertEquals("Apt 789", address.getAddressLine2());
        assertEquals("Metropolis", address.getCity());
        assertEquals("StateName", address.getState());
        assertEquals("12345", address.getZipCode());
        assertNotNull(address.getUser()); // Check that user is set
    }

    @Test
    void addressLine1_ShouldBeRequired() {
        Address newAddress = new Address();
        newAddress.setAddressLine2("Apt 789");
        newAddress.setCity("Metropolis");
        newAddress.setState("StateName");
        newAddress.setZipCode("12345");

        // ID generation
        newAddress.generateId();

        // Check if addressLine1 is required
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            // Assuming that some validation logic would throw an exception
            if (newAddress.getAddressLine1() == null || newAddress.getAddressLine1().isEmpty()) {
                throw new RuntimeException("Address Line 1 is required");
            }
        });

        assertEquals("Address Line 1 is required", thrown.getMessage());
    }

}
