package com.crimsonlogic.bankloanmanagementsystem.controller;

import com.crimsonlogic.bankloanmanagementsystem.dto.PasswordChangeRequest;
import com.crimsonlogic.bankloanmanagementsystem.dto.UserRegistrationDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.User;
import com.crimsonlogic.bankloanmanagementsystem.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testGetUserDetails_Success() throws Exception {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        // Create a new MockHttpSession and set the user attribute
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        mockMvc.perform(get("/api/user/details")
                .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void testGetUserDetails_Unauthorized() throws Exception {
        // Create a new MockHttpSession without setting any user attribute
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(get("/api/user/details")
                .session(session))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetUserProfile() throws Exception {
        UserRegistrationDTO userDTO = new UserRegistrationDTO();
        userDTO.setFirstName("Jane");
        userDTO.setLastName("Doe");

        when(userService.getUserProfile("USER123")).thenReturn(userDTO);

        mockMvc.perform(get("/api/user/edit/{userId}", "USER123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void testUpdateUserProfile() throws Exception {
        UserRegistrationDTO userDTO = new UserRegistrationDTO();
        userDTO.setFirstName("Jane");
        userDTO.setLastName("Doe");

        User updatedUser = new User();
        updatedUser.setFirstName("Jane");
        updatedUser.setLastName("Doe");

        when(userService.updateUserProfile(anyString(), any(UserRegistrationDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/user/edit/{userId}", "USER123")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Jane\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(post("/api/user/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logout successful"));
    }

    @Test
    void testChangePassword_Success() throws Exception {
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setUserId("USER123");
        request.setCurrentPassword("oldPassword");
        request.setNewPassword("newPassword");

        doNothing().when(userService).changePassword(anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/user/changepassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":\"USER123\",\"currentPassword\":\"oldPassword\",\"newPassword\":\"newPassword\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password updated successfully"));
    }

    @Test
    void testChangePassword_UserNotFound() throws Exception {
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setUserId("USER123");
        request.setCurrentPassword("oldPassword");
        request.setNewPassword("newPassword");

        doThrow(new RuntimeException("User not found")).when(userService).changePassword(anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/user/changepassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":\"USER123\",\"currentPassword\":\"oldPassword\",\"newPassword\":\"newPassword\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    @Test
    void testChangePassword_InvalidPassword() throws Exception {
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setUserId("USER123");
        request.setCurrentPassword("oldPassword");
        request.setNewPassword("newPassword");

        doThrow(new IllegalArgumentException("Invalid password")).when(userService).changePassword(anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/user/changepassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":\"USER123\",\"currentPassword\":\"oldPassword\",\"newPassword\":\"newPassword\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid password"));
    }
}
