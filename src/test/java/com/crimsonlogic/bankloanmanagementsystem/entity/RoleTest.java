package com.crimsonlogic.bankloanmanagementsystem.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.crimsonlogic.bankloanmanagementsystem.util.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
    }

    @Test
    void testGenerateId() {
        // Verify that the ID is generated before persisting
        role.generateId();
        assertNotNull(role.getRoleId());
        assertTrue(role.getRoleId().startsWith("ROLE"));
    }

    @Test
    void testRoleDetails() {
        // Set role details
        role.setRoleName("Admin");

        // Validate role details
        assertEquals("Admin", role.getRoleName());
    }

    
}
