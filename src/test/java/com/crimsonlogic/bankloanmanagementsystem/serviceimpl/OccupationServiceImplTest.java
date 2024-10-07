package com.crimsonlogic.bankloanmanagementsystem.serviceimpl;

import com.crimsonlogic.bankloanmanagementsystem.entity.Occupation;
import com.crimsonlogic.bankloanmanagementsystem.entity.User;
import com.crimsonlogic.bankloanmanagementsystem.repository.OccupationRepository;
import com.crimsonlogic.bankloanmanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class OccupationServiceImplTest {

    @InjectMocks
    private OccupationServiceImpl occupationService;

    @Mock
    private OccupationRepository occupationRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private Occupation occupation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserId("USER123");
        occupation = new Occupation();
        occupation.setOccupationName("Software Engineer");
    }

    @Test
    void testSaveOccupation() {
        // Mocking the repository to do nothing on save
        when(occupationRepository.save(any(Occupation.class))).thenReturn(occupation);

        // Call the method to test
        occupationService.saveOccupation(occupation);

        // Verify that the save method was called
        verify(occupationRepository, times(1)).save(occupation);
    }

    @Test
    void testGetOccupationByUserId_Success() {
        // Mocking a user occupation
        when(occupationRepository.findByUserUserId(anyString())).thenReturn(occupation);

        // Call the method to test
        Occupation foundOccupation = occupationService.getOccupationByUserId("USER123");

        // Assertions
        assertNotNull(foundOccupation);
        assertEquals("Software Engineer", foundOccupation.getOccupationName());
        verify(occupationRepository, times(1)).findByUserUserId("USER123");
    }

    @Test
    void testGetOccupationByUserId_NotFound() {
        // Mocking a case where no occupation is found for the user
        when(occupationRepository.findByUserUserId(anyString())).thenReturn(null);

        // Call the method to test
        Occupation foundOccupation = occupationService.getOccupationByUserId("USER456");

        // Assertions
        assertNull(foundOccupation);
        verify(occupationRepository, times(1)).findByUserUserId("USER456");
    }

    @Test
    void testSaveOccupationByUserId_Success() {
        // Mocking the user repository to return a user
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(occupationRepository.save(any(Occupation.class))).thenReturn(occupation);

        // Call the method to test
        Occupation savedOccupation = occupationService.saveOccupation("USER123", occupation);

        // Assertions
        assertNotNull(savedOccupation);
        assertEquals("Software Engineer", savedOccupation.getOccupationName());
        verify(userRepository, times(1)).findById("USER123");
        verify(occupationRepository, times(1)).save(occupation);
    }

    @Test
    void testSaveOccupationByUserId_UserNotFound() {
        // Mocking the user repository to throw an exception when user is not found
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        // Call the method to test and verify exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            occupationService.saveOccupation("USER456", occupation);
        });

        // Assertions
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById("USER456");
        verify(occupationRepository, times(0)).save(any(Occupation.class));
    }
}
