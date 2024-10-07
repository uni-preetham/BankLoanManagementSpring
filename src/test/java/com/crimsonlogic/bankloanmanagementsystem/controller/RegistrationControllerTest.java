package com.crimsonlogic.bankloanmanagementsystem.controller;
import com.crimsonlogic.bankloanmanagementsystem.dto.ManagerRegistrationDTO;
import com.crimsonlogic.bankloanmanagementsystem.dto.UserRegistrationDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.Bank;
import com.crimsonlogic.bankloanmanagementsystem.exception.UserAlreadyExistsException;
import com.crimsonlogic.bankloanmanagementsystem.service.ManagerService;
import com.crimsonlogic.bankloanmanagementsystem.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ManagerService managerService;

    @InjectMocks
    private RegistrationController registrationController;

    @Test
    void testRegisterUser_Success() throws Exception {
        UserRegistrationDTO userDTO = new UserRegistrationDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");

        // Mock user registration service
        doNothing().when(userService).registerUser(any(UserRegistrationDTO.class));

        mockMvc.perform(post("/api/register/registeruser")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\", \"firstName\":\"John\", \"lastName\":\"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    void testRegisterUser_UserAlreadyExists() throws Exception {
        UserRegistrationDTO userDTO = new UserRegistrationDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");

        // Mock user registration service to throw UserAlreadyExistsException
        doThrow(new UserAlreadyExistsException("User already exists")).when(userService).registerUser(any(UserRegistrationDTO.class));

        mockMvc.perform(post("/api/register/registeruser")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\", \"firstName\":\"John\", \"lastName\":\"Doe\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User already exists"));
    }

    @Test
    void testRegisterManager_Success() throws Exception {
        ManagerRegistrationDTO managerDTO = new ManagerRegistrationDTO();
        managerDTO.setEmail("manager@example.com");
        managerDTO.setFirstName("Alice");
        managerDTO.setLastName("Smith");

        // Set up a mock bank in session
        MockHttpSession session = new MockHttpSession();
        Bank bank = new Bank();
        bank.setBankId("BANK123");
        session.setAttribute("bank", bank);

        // Mock manager registration service
        doNothing().when(managerService).registerManager(any(ManagerRegistrationDTO.class), any(Bank.class));

        mockMvc.perform(post("/api/register/registermanager")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"manager@example.com\", \"firstName\":\"Alice\", \"lastName\":\"Smith\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Manager registered successfully"));
    }

    @Test
    void testRegisterManager_BankNotFoundInSession() throws Exception {
        ManagerRegistrationDTO managerDTO = new ManagerRegistrationDTO();
        managerDTO.setEmail("manager@example.com");
        managerDTO.setFirstName("Alice");
        managerDTO.setLastName("Smith");

        // Create a new session without a bank attribute
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/api/register/registermanager")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"manager@example.com\", \"firstName\":\"Alice\", \"lastName\":\"Smith\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bank not found in session"));
    }
}
