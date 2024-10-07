package com.crimsonlogic.bankloanmanagementsystem.controller;

import com.crimsonlogic.bankloanmanagementsystem.entity.Occupation;
import com.crimsonlogic.bankloanmanagementsystem.entity.User;
import com.crimsonlogic.bankloanmanagementsystem.service.OccupationService;
import com.crimsonlogic.bankloanmanagementsystem.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class OccupationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private OccupationService occupationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private OccupationController occupationController;

    private HttpSession session;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(occupationController).build();
        session = new MockHttpSession(); // Creating a mock session for testing
    }

    @Test
    void testGetOccupationByUserId() throws Exception {
        // Mock data
        Occupation occupation = new Occupation();
        occupation.setOccupationType("Software Engineer");
        occupation.setOccupationName("Engineer");
        occupation.setCompanyName("Tech Inc.");
        occupation.setSalary(80000.0);

        // Mock the service
        when(occupationService.getOccupationByUserId(anyString())).thenReturn(occupation);

        // Perform the GET request
        mockMvc.perform(get("/api/occupation/{userId}", "USER123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.occupationType").value("Software Engineer"))
                .andExpect(jsonPath("$.occupationName").value("Engineer"));
    }

    @Test
    void testAddOrUpdateOccupation() throws Exception {
        // Mock data
        Occupation occupation = new Occupation();
        occupation.setOccupationType("Software Engineer");
        occupation.setOccupationName("Engineer");
        occupation.setCompanyName("Tech Inc.");
        occupation.setSalary(80000.0);
        
        User user = new User(); // Mock user object, you can set fields as needed

        // Mock the service
        when(occupationService.saveOccupation(anyString(), any(Occupation.class))).thenReturn(occupation);
        when(userService.getUser(anyString())).thenReturn(user);

        // Perform the POST request
        mockMvc.perform(post("/api/occupation/add/{userId}", "USER123")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"occupationType\":\"Software Engineer\",\"occupationName\":\"Engineer\",\"companyName\":\"Tech Inc.\",\"salary\":80000.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.occupationType").value("Software Engineer"))
                .andExpect(jsonPath("$.occupationName").value("Engineer"));

        // Check if user details were set in the session
        session.setAttribute("user", user);
        // You can assert the session values here if needed
    }
}
