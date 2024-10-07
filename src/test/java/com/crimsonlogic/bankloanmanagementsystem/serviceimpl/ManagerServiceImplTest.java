package com.crimsonlogic.bankloanmanagementsystem.serviceimpl;

import com.crimsonlogic.bankloanmanagementsystem.dto.ManagerDTO;
import com.crimsonlogic.bankloanmanagementsystem.dto.ManagerRegistrationDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.Bank;
import com.crimsonlogic.bankloanmanagementsystem.entity.Login;
import com.crimsonlogic.bankloanmanagementsystem.entity.Manager;
import com.crimsonlogic.bankloanmanagementsystem.entity.Role;
import com.crimsonlogic.bankloanmanagementsystem.repository.LoginRepository;
import com.crimsonlogic.bankloanmanagementsystem.repository.ManagerRepository;
import com.crimsonlogic.bankloanmanagementsystem.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ManagerServiceImplTest {

    @InjectMocks
    private ManagerServiceImpl managerService;

    @Mock
    private LoginRepository loginRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private ManagerRegistrationDTO managerRegistrationDTO;
    private Bank bank;
    private Manager manager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initializing test data
        managerRegistrationDTO = new ManagerRegistrationDTO();
        managerRegistrationDTO.setFirstName("John");
        managerRegistrationDTO.setLastName("Doe");
        managerRegistrationDTO.setEmail("john.doe@example.com");
        managerRegistrationDTO.setPhone("1234567890");
        managerRegistrationDTO.setPassword("Password123!");

        bank = new Bank(); // Assuming Bank entity has default constructor
        bank.setBankId("BANK123"); // Set a sample bank ID

        manager = new Manager();
        manager.setFirstName("John");
        manager.setLastName("Doe");
        manager.setEmail("john.doe@example.com");
        manager.setPhone("1234567890");
    }

    @Test
    void testRegisterManager() {
        // Mocking behavior
        Role managerRole = new Role();
        managerRole.setRoleName("MANAGER");
        when(roleRepository.findByRoleName("MANAGER")).thenReturn(managerRole);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // Call the method to test
        managerService.registerManager(managerRegistrationDTO, bank);

        // Verify interactions
        verify(loginRepository, times(1)).save(any(Login.class)); // Check login save
        verify(managerRepository, times(1)).save(any(Manager.class)); // Check manager save
    }

    @Test
    void testFindByLoginId_Success() {
        // Mocking manager retrieval
        when(managerRepository.findByLogin_LoginId(anyString())).thenReturn(manager);

        // Call the method to test
        Manager foundManager = managerService.findByLoginId("LOGIN123");

        // Assertions
        assertNotNull(foundManager);
        assertEquals("John", foundManager.getFirstName());
        verify(managerRepository, times(1)).findByLogin_LoginId("LOGIN123");
    }

    @Test
    void testFindByLoginId_NotFound() {
        // Mocking behavior for not found
        when(managerRepository.findByLogin_LoginId(anyString())).thenReturn(null);

        // Call the method to test
        Manager foundManager = managerService.findByLoginId("LOGIN456");

        // Assertions
        assertNull(foundManager);
        verify(managerRepository, times(1)).findByLogin_LoginId("LOGIN456");
    }

    @Test
    void testUpdateManagerProfile_Success() {
        // Mocking behavior
        when(managerRepository.findById(anyString())).thenReturn(Optional.of(manager));

        ManagerDTO managerDTO = new ManagerDTO();
        managerDTO.setFirstName("Jane");
        managerDTO.setLastName("Smith");
        managerDTO.setEmail("jane.smith@example.com");
        managerDTO.setPhone("0987654321");

        // Call the method to test
        Manager updatedManager = managerService.updateManagerProfile("MANAGER123", managerDTO);

        // Assertions
        assertNotNull(updatedManager);
        assertEquals("Jane", updatedManager.getFirstName());
        assertEquals("Smith", updatedManager.getLastName());
        assertEquals("jane.smith@example.com", updatedManager.getEmail());
        assertEquals("0987654321", updatedManager.getPhone());
        verify(managerRepository, times(1)).save(updatedManager);
    }

    @Test
    void testUpdateManagerProfile_NotFound() {
        // Mocking behavior for not found
        when(managerRepository.findById(anyString())).thenReturn(Optional.empty());

        ManagerDTO managerDTO = new ManagerDTO();

        // Call the method to test and verify exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            managerService.updateManagerProfile("MANAGER456", managerDTO);
        });

        // Assertions
        assertEquals("Manager not found with id: MANAGER456", exception.getMessage());
        verify(managerRepository, times(1)).findById("MANAGER456");
    }

    @Test
    void testGetAllManagers() {
        // Mocking behavior
        List<Manager> managers = new ArrayList<>();
        managers.add(manager);
        when(managerRepository.findAll()).thenReturn(managers);

        // Call the method to test
        List<Manager> allManagers = managerService.getAllManagers();

        // Assertions
        assertNotNull(allManagers);
        assertEquals(1, allManagers.size());
        verify(managerRepository, times(1)).findAll();
    }

    @Test
    void testDeleteManagerById() {
        // Call the method to test
        managerService.deleteManagerById("MANAGER123");

        // Verify interaction
        verify(managerRepository, times(1)).deleteById("MANAGER123");
    }
}
