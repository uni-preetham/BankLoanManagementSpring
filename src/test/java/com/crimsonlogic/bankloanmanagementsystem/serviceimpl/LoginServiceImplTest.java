package com.crimsonlogic.bankloanmanagementsystem.serviceimpl;

import com.crimsonlogic.bankloanmanagementsystem.entity.Login;
import com.crimsonlogic.bankloanmanagementsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.bankloanmanagementsystem.repository.LoginRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class LoginServiceImplTest {

    @InjectMocks
    private LoginServiceImpl loginService;

    @Mock
    private LoginRepository loginRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private Login login;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initializing test data
        login = new Login();
        login.setEmail("john.doe@example.com");
        login.setPassword("encodedPassword");
    }

    @Test
    void testAuthenticate_Success() {
        // Mocking behavior
        when(loginRepository.findByEmail(anyString())).thenReturn(Optional.of(login));
        when(passwordEncoder.matches("Password123!", login.getPassword())).thenReturn(true);

        // Call the method to test
        Login authenticatedLogin = loginService.authenticate("john.doe@example.com", "Password123!");

        // Assertions
        assertNotNull(authenticatedLogin);
        assertEquals("john.doe@example.com", authenticatedLogin.getEmail());
        verify(loginRepository, times(1)).findByEmail("john.doe@example.com");
        verify(passwordEncoder, times(1)).matches("Password123!", login.getPassword());
    }

    @Test
    void testAuthenticate_UserNotFound() {
        // Mocking behavior for not found
        when(loginRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Call the method to test and verify exception is thrown
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            loginService.authenticate("unknown@example.com", "Password123!");
        });

        // Assertions
        assertEquals("Email unknown@example.com not found", exception.getMessage());
        verify(loginRepository, times(1)).findByEmail("unknown@example.com");
    }

    @Test
    void testAuthenticate_InvalidPassword() {
        // Mocking behavior
        when(loginRepository.findByEmail(anyString())).thenReturn(Optional.of(login));
        when(passwordEncoder.matches("InvalidPassword", login.getPassword())).thenReturn(false);

        // Call the method to test
        Login authenticatedLogin = loginService.authenticate("john.doe@example.com", "InvalidPassword");

        // Assertions
        assertNull(authenticatedLogin);
        verify(loginRepository, times(1)).findByEmail("john.doe@example.com");
        verify(passwordEncoder, times(1)).matches("InvalidPassword", login.getPassword());
    }
}
