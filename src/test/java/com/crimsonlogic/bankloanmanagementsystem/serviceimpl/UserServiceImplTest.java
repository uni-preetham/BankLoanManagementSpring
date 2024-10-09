package com.crimsonlogic.bankloanmanagementsystem.serviceimpl;

import com.crimsonlogic.bankloanmanagementsystem.dto.UserRegistrationDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.Login;
import com.crimsonlogic.bankloanmanagementsystem.entity.Role;
import com.crimsonlogic.bankloanmanagementsystem.entity.User;
import com.crimsonlogic.bankloanmanagementsystem.exception.UserAlreadyExistsException;
import com.crimsonlogic.bankloanmanagementsystem.repository.LoginRepository;
import com.crimsonlogic.bankloanmanagementsystem.repository.RoleRepository;
import com.crimsonlogic.bankloanmanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoginRepository loginRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Login login;
    private Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Initialize test data
        role = new Role();
        role.setRoleName("USER");
        
        login = new Login();
        login.setEmail("test@example.com");
        login.setPassword("encodedPassword");
        login.setRole(role);
        
        user = new User();
        user.setLogin(login);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhone("1234567890");
        user.setCreditScore(800);
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setEmail("test@example.com");
        registrationDTO.setFirstName("John");
        registrationDTO.setLastName("Doe");
        registrationDTO.setPhone("1234567890");
        registrationDTO.setPassword("password123");

        when(loginRepository.findByEmail(any(String.class))).thenReturn(java.util.Optional.of(login));

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerUser(registrationDTO);
        });

        assertEquals("User with this email already exists.", exception.getMessage());
    }

    

    @Test
    void testGetUserProfile_UserNotFound() {
        when(userRepository.findById(any(String.class))).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserProfile("nonexistentId");
        });

        assertEquals("User not found with id: nonexistentId", exception.getMessage());
    }

    


    
}
