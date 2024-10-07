package com.crimsonlogic.bankloanmanagementsystem.controller;

import com.crimsonlogic.bankloanmanagementsystem.dto.LoginRequestDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.Login;
import com.crimsonlogic.bankloanmanagementsystem.entity.Role;
import com.crimsonlogic.bankloanmanagementsystem.entity.User;
import com.crimsonlogic.bankloanmanagementsystem.entity.Manager;
import com.crimsonlogic.bankloanmanagementsystem.entity.Bank;
import com.crimsonlogic.bankloanmanagementsystem.service.LoginService;
import com.crimsonlogic.bankloanmanagementsystem.service.UserService;
import com.crimsonlogic.bankloanmanagementsystem.service.ManagerService;
import com.crimsonlogic.bankloanmanagementsystem.service.BankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.servlet.http.HttpSession;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

class LoginControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private LoginController loginController;

    @Mock
    private LoginService loginService;

    @Mock
    private UserService userService;

    @Mock
    private ManagerService managerService;

    @Mock
    private BankService bankService;

    @Mock
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    void testLoginUserAsUser() throws Exception {
        // Prepare data
        LoginRequestDTO loginRequest = new LoginRequestDTO("user@example.com", "password");
        Login login = new Login();
        login.setLoginId("LOG001");
        Role role = new Role();
        role.setRoleName("USER");
        login.setRole(role);
        
        User user = new User();
        user.setFirstName("John");
        
        // Mock behavior
        when(loginService.authenticate(loginRequest.getEmail(), loginRequest.getPassword())).thenReturn(login);
        when(userService.findByLoginId(login.getLoginId())).thenReturn(user);

        // Perform request and assertions
        mockMvc.perform(post("/api/login/loginuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"user@example.com\",\"password\":\"password\"}")
                .session(session)) // Pass the mocked session
                .andExpect(status().isOk())
                .andExpect(content().string("/user/dashboard"));

        // Verify session storage
        verify(session, times(1)).setAttribute("user", user);
    }

    @Test
    void testLoginUserAsManager() throws Exception {
        // Prepare data
        LoginRequestDTO loginRequest = new LoginRequestDTO("manager@example.com", "password");
        Login login = new Login();
        login.setLoginId("LOG002");
        Role role = new Role();
        role.setRoleName("MANAGER");
        login.setRole(role);
        
        Manager manager = new Manager();
        manager.setFirstName("Jane");
        
        // Mock behavior
        when(loginService.authenticate(loginRequest.getEmail(), loginRequest.getPassword())).thenReturn(login);
        when(managerService.findByLoginId(login.getLoginId())).thenReturn(manager);

        // Perform request and assertions
        mockMvc.perform(post("/api/login/loginuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"manager@example.com\",\"password\":\"password\"}")
                .session(session)) // Pass the mocked session
                .andExpect(status().isOk())
                .andExpect(content().string("/manager/dashboard"));

        // Verify session storage
        verify(session, times(1)).setAttribute("manager", manager);
    }

    @Test
    void testLoginUserAsBank() throws Exception {
        // Prepare data
        LoginRequestDTO loginRequest = new LoginRequestDTO("bank@example.com", "password");
        Login login = new Login();
        login.setLoginId("LOG003");
        Role role = new Role();
        role.setRoleName("BANK");
        login.setRole(role);
        
        Bank bank = new Bank();
        bank.setBankName("MyBank");
        
        // Mock behavior
        when(loginService.authenticate(loginRequest.getEmail(), loginRequest.getPassword())).thenReturn(login);
        when(bankService.findByLoginId(login.getLoginId())).thenReturn(bank);

        // Perform request and assertions
        mockMvc.perform(post("/api/login/loginuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"bank@example.com\",\"password\":\"password\"}")
                .session(session)) // Pass the mocked session
                .andExpect(status().isOk())
                .andExpect(content().string("/bank/dashboard"));

        // Verify session storage
        verify(session, times(1)).setAttribute("bank", bank);
    }

    @Test
    void testLoginUserWithInvalidCredentials() throws Exception {
        // Prepare data
        LoginRequestDTO loginRequest = new LoginRequestDTO("wrong@example.com", "wrongpassword");
        
        // Mock behavior
        when(loginService.authenticate(loginRequest.getEmail(), loginRequest.getPassword())).thenReturn(null);

        // Perform request and assertions
        mockMvc.perform(post("/api/login/loginuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"wrong@example.com\",\"password\":\"wrongpassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    void testLoginUserWithUnauthorizedRole() throws Exception {
        // Prepare data
        LoginRequestDTO loginRequest = new LoginRequestDTO("user@example.com", "password");
        Login login = new Login();
        login.setLoginId("LOG001");
        Role role = new Role();
        role.setRoleName("UNKNOWN");  // Role that doesn't exist
        login.setRole(role);
        
        // Mock behavior
        when(loginService.authenticate(loginRequest.getEmail(), loginRequest.getPassword())).thenReturn(login);

        // Perform request and assertions
        mockMvc.perform(post("/api/login/loginuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"user@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Unauthorized access"));
    }
}
