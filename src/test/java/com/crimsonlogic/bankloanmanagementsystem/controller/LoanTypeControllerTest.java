package com.crimsonlogic.bankloanmanagementsystem.controller;
import com.crimsonlogic.bankloanmanagementsystem.dto.LoanTypeDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.Bank;
import com.crimsonlogic.bankloanmanagementsystem.entity.LoanType;
import com.crimsonlogic.bankloanmanagementsystem.service.LoanTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LoanTypeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LoanTypeService loanTypeService;

    @Mock
    private MockHttpSession session;

    @InjectMocks
    private LoanTypeController loanTypeController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loanTypeController).build();
    }

    @Test
    void testAddLoanType_BankFound() throws Exception {
        // Prepare data
        LoanTypeDTO loanTypeDTO = new LoanTypeDTO(); // Set necessary fields
        Bank bank = new Bank(); // Initialize and set necessary fields
        when(session.getAttribute("bank")).thenReturn(bank);
        
        // Perform request and assertions
        mockMvc.perform(post("/api/loans/add")
                .contentType("application/json")
                .content("{\"loanName\":\"Home Loan\", \"interestRate\":5.5, \"maxAmount\":500000, \"durationYears\":15}")
                .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string("Loan details added successfully"));

        // Verify service interaction
        verify(loanTypeService, times(1)).addLoanType(any(LoanTypeDTO.class), eq(bank));
    }

    @Test
    void testAddLoanType_BankNotFound() throws Exception {
        // Prepare data
        LoanTypeDTO loanTypeDTO = new LoanTypeDTO(); // Set necessary fields
        when(session.getAttribute("bank")).thenReturn(null); // Simulate bank not found
        
        // Perform request and assertions
        mockMvc.perform(post("/api/loans/add")
                .contentType("application/json")
                .content("{\"loanName\":\"Home Loan\", \"interestRate\":5.5, \"maxAmount\":500000, \"durationYears\":15}")
                .session(session))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bank not found in session"));

        // Verify service interaction
        verify(loanTypeService, times(0)).addLoanType(any(LoanTypeDTO.class), any(Bank.class));
    }

    @Test
    void testGetLoanTypeById_Found() throws Exception {
        // Prepare data
        String loanTypeId = "L001";
        LoanType loanType = new LoanType(); // Initialize and set necessary fields
        when(loanTypeService.getLoanTypeById(loanTypeId)).thenReturn(loanType);
        
        // Perform request and assertions
        mockMvc.perform(get("/api/loans/{loanTypeId}", loanTypeId))
                .andExpect(status().isOk())
                .andExpect(content().json("{ /* JSON representation of loanType */ }"));

        // Verify service interaction
        verify(loanTypeService, times(1)).getLoanTypeById(loanTypeId);
    }

    @Test
    void testGetLoanTypeById_NotFound() throws Exception {
        // Prepare data
        String loanTypeId = "L002";
        when(loanTypeService.getLoanTypeById(loanTypeId)).thenReturn(null); // Simulate loan type not found
        
        // Perform request and assertions
        mockMvc.perform(get("/api/loans/{loanTypeId}", loanTypeId))
                .andExpect(status().isNotFound());

        // Verify service interaction
        verify(loanTypeService, times(1)).getLoanTypeById(loanTypeId);
    }

    @Test
    void testEditLoanType() throws Exception {
        // Prepare data
        String loanTypeId = "L001";
        LoanTypeDTO loanTypeDTO = new LoanTypeDTO(); // Set necessary fields

        // Perform request and assertions
        mockMvc.perform(put("/api/loans/edit/{loanTypeId}", loanTypeId)
                .contentType("application/json")
                .content("{\"loanName\":\"Updated Loan\", \"interestRate\":6.5, \"maxAmount\":600000, \"durationYears\":20}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Loan type updated successfully"));

        // Verify service interaction
        verify(loanTypeService, times(1)).editLoanType(eq(loanTypeId), any(LoanTypeDTO.class));
    }

    @Test
    void testGetLoanTypesByBank_BankFound() throws Exception {
        // Prepare data
        Bank bank = new Bank(); // Initialize and set necessary fields
        List<LoanType> loanTypes = new ArrayList<>(); // Prepare a list of loan types
        loanTypes.add(new LoanType()); // Add some loan types to the list
        
        when(session.getAttribute("bank")).thenReturn(bank); // Simulate bank found
        when(loanTypeService.getLoanTypesByBank(bank)).thenReturn(loanTypes);
        
        // Perform request and assertions
        mockMvc.perform(get("/api/loans/all").session(session))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}]")); // Adjust according to your LoanType representation

        // Verify service interaction
        verify(loanTypeService, times(1)).getLoanTypesByBank(bank);
    }

    @Test
    void testGetLoanTypesByBank_BankNotFound() throws Exception {
        // Prepare data
        when(session.getAttribute("bank")).thenReturn(null); // Simulate bank not found
        
        // Perform request and assertions
        mockMvc.perform(get("/api/loans/all").session(session))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("")); // Check for an empty response body

        // Verify service interaction
        verify(loanTypeService, times(0)).getLoanTypesByBank(any(Bank.class));
    }

    @Test
    void testDeleteLoanType_Success() throws Exception {
        // Prepare data
        String loanTypeId = "L001";
        
        // Perform request and assertions
        mockMvc.perform(delete("/api/loans/delete/{loanTypeId}", loanTypeId))
                .andExpect(status().isOk())
                .andExpect(content().string("Loan type deleted successfully"));

        // Verify service interaction
        verify(loanTypeService, times(1)).deleteLoanTypeById(loanTypeId);
    }

    @Test
    void testDeleteLoanType_NotFound() throws Exception {
        // Prepare data
        String loanTypeId = "L002";
        doThrow(new RuntimeException("Loan type not found")).when(loanTypeService).deleteLoanTypeById(loanTypeId);
        
        // Perform request and assertions
        mockMvc.perform(delete("/api/loans/delete/{loanTypeId}", loanTypeId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Loan type not found"));

        // Verify service interaction
        verify(loanTypeService, times(1)).deleteLoanTypeById(loanTypeId);
    }
}
