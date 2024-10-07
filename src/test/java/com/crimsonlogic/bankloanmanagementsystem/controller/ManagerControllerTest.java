package com.crimsonlogic.bankloanmanagementsystem.controller;

import com.crimsonlogic.bankloanmanagementsystem.dto.LoanRequestDTO;
import com.crimsonlogic.bankloanmanagementsystem.dto.ManagerDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.LoanRequest;
import com.crimsonlogic.bankloanmanagementsystem.entity.Manager;
import com.crimsonlogic.bankloanmanagementsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.bankloanmanagementsystem.service.LoanRequestService;
import com.crimsonlogic.bankloanmanagementsystem.service.ManagerService;

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


import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private LoanRequestService loanRequestService;

    @Mock
    private ManagerService managerService;

    @InjectMocks
    private ManagerController managerController;

    private MockHttpSession session;
    private Manager mockManager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(managerController).build();
        session = new MockHttpSession();

        // Initialize a mock manager for testing
        mockManager = new Manager();
        mockManager.setManagerId("MAN001");
        mockManager.setFirstName("John");
        mockManager.setLastName("Doe");
        mockManager.setEmail("john.doe@example.com");
        mockManager.setPhone("1234567890");
    }

    @Test
    void testUpdateManagerProfile() throws Exception {
        // Prepare the ManagerDTO with updated values
        ManagerDTO managerDTO = new ManagerDTO();
        managerDTO.setFirstName("John Updated");
        managerDTO.setLastName("Doe Updated");
        managerDTO.setEmail("john.updated@example.com");
        managerDTO.setPhone("0987654321");

        // Initialize the mock manager with updated values
        Manager mockManager = new Manager();
        mockManager.setFirstName("John Updated");
        mockManager.setLastName("Doe Updated");
        mockManager.setEmail("john.updated@example.com");
        mockManager.setPhone("0987654321");

        when(managerService.updateManagerProfile(eq("MAN001"), any(ManagerDTO.class))).thenReturn(mockManager);
        
        // Set the manager in session
        session.setAttribute("manager", mockManager);

        // Perform the PUT request to update the manager profile
        mockMvc.perform(put("/api/manager/editmanager/{managerId}", "MAN001")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"John Updated\",\"lastName\":\"Doe Updated\",\"email\":\"john.updated@example.com\",\"phone\":\"0987654321\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John Updated"))
                .andExpect(jsonPath("$.lastName").value("Doe Updated"));

        // Verify that the manager is updated in the session
        verify(managerService, times(1)).updateManagerProfile("MAN001", managerDTO);
    }


    @Test
    void testGetManagerDetails() throws Exception {
        // Set the manager in session
        session.setAttribute("manager", mockManager);

        mockMvc.perform(get("/api/manager/details").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void testGetLoanRequestsForManager() throws Exception {
        List<LoanRequestDTO> loanRequests = new ArrayList<>();
        loanRequests.add(new LoanRequestDTO()); // Add mock LoanRequestDTO instances as needed

        // Set the manager in session
        session.setAttribute("manager", mockManager);
        when(loanRequestService.getAllLoanRequestsForManager(anyString())).thenReturn(loanRequests);

        mockMvc.perform(get("/api/manager/loan-requests").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());

        verify(loanRequestService, times(1)).getAllLoanRequestsForManager("MAN001");
    }

    @Test
    void testVerifyLoanRequest() throws Exception {
        LoanRequest mockLoanRequest = new LoanRequest();
        mockLoanRequest.setStatus("Verified");

        when(loanRequestService.verifyLoanRequest(anyString(), anyString())).thenReturn(mockLoanRequest);

        mockMvc.perform(put("/api/manager/verify/{loanRequestId}", "REQ001")
                .param("status", "Verified"))
                .andExpect(status().isOk())
                .andExpect(content().string("Loan request verified by manager"));

        verify(loanRequestService, times(1)).verifyLoanRequest("REQ001", "Verified");
    }

    @Test
    void testApproveLoanRequest() throws Exception {
        // Set the manager in session
        session.setAttribute("manager", mockManager);

        mockMvc.perform(put("/api/manager/loan-requests/approve/{requestId}", "REQ001").session(session))
                .andExpect(status().isOk())
                .andExpect(content().string("Loan request approved"));

        verify(loanRequestService, times(1)).updateLoanRequestStatus("REQ001", "Verified", mockManager);
    }

    @Test
    void testRejectLoanRequest() throws Exception {
        // Set the manager in session
        session.setAttribute("manager", mockManager);

        mockMvc.perform(put("/api/manager/loan-requests/reject/{requestId}", "REQ001").session(session))
                .andExpect(status().isOk())
                .andExpect(content().string("Loan request rejected"));

        verify(loanRequestService, times(1)).updateLoanRequestStatus("REQ001", "Rejected", mockManager);
    }

    @Test
    void testGetAllManagers() throws Exception {
        List<Manager> managers = new ArrayList<>();
        managers.add(mockManager);

        when(managerService.getAllManagers()).thenReturn(managers);

        mockMvc.perform(get("/api/manager/getallmanagers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());

        verify(managerService, times(1)).getAllManagers();
    }

    @Test
    void testDeleteManagerById() throws Exception {
        doNothing().when(managerService).deleteManagerById(anyString());

        mockMvc.perform(delete("/api/manager/deletemanager/{managerId}", "MAN001"))
                .andExpect(status().isNoContent());

        verify(managerService, times(1)).deleteManagerById("MAN001");
    }
}
