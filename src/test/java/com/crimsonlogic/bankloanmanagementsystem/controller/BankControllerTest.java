package com.crimsonlogic.bankloanmanagementsystem.controller;

import com.crimsonlogic.bankloanmanagementsystem.dto.BankResponseDto;
import com.crimsonlogic.bankloanmanagementsystem.dto.LoanRequestDTO;
import com.crimsonlogic.bankloanmanagementsystem.dto.RevenueDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.Bank;
import com.crimsonlogic.bankloanmanagementsystem.entity.LoanRequest;
import com.crimsonlogic.bankloanmanagementsystem.service.BankService;
import com.crimsonlogic.bankloanmanagementsystem.service.LoanRequestService;
import com.crimsonlogic.bankloanmanagementsystem.service.PaymentDetailService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class BankControllerTest {

    @InjectMocks
    private BankController bankController;

    @Mock
    private BankService bankService;

    @Mock
    private LoanRequestService loanRequestService;

    @Mock
    private PaymentDetailService paymentService;

    @Mock
    private HttpSession session;

    private Bank bank;
    private LoanRequest loanRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bank = new Bank();
        bank.setBankId("BANK1");
        bank.setBankName("Bank A");

        loanRequest = new LoanRequest();
        loanRequest.setRequestId("LOAN1");
        loanRequest.setStatus("Approved");
    }

    @Test
    void testGetBanksWithLoans() {
        List<BankResponseDto> banks = new ArrayList<>();
        when(bankService.findAllBanksWithLoans()).thenReturn(banks);

        List<BankResponseDto> result = bankController.getBanksWithLoans();

        assertEquals(banks, result);
        verify(bankService).findAllBanksWithLoans();
    }

    @Test
    void testGetBankDetails_WithBankInSession() {
        when(session.getAttribute("bank")).thenReturn(bank);

        ResponseEntity<Bank> response = bankController.getBankDetails(session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bank, response.getBody());
    }

    @Test
    void testGetBankDetails_NoBankInSession() {
        when(session.getAttribute("bank")).thenReturn(null);

        ResponseEntity<Bank> response = bankController.getBankDetails(session);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testApproveLoanRequest() {
        when(loanRequestService.approveLoanRequest(anyString(), anyString())).thenReturn(loanRequest);

        ResponseEntity<String> response = bankController.approveLoanRequest("LOAN1", "Approved");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Loan request approved by bank", response.getBody());
    }

    @Test
    void testGetVerifiedLoanRequests() {
        when(session.getAttribute("bank")).thenReturn(bank);
        List<LoanRequestDTO> loanRequests = new ArrayList<>();
        when(loanRequestService.getVerifiedLoanRequestsForBank(bank.getBankId())).thenReturn(loanRequests);

        ResponseEntity<List<LoanRequestDTO>> response = bankController.getVerifiedLoanRequests(session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loanRequests, response.getBody());
    }

    @Test
    void testUpdateLoanStatus() {
        doNothing().when(loanRequestService).updateLoanRequestStatus(anyString(), anyString());

        ResponseEntity<String> response = bankController.updateLoanStatus("LOAN1", "Approved");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Loan request status updated successfully", response.getBody());
    }

    @Test
    void testGetMonthlyRevenue_WithBankInSession() {
        when(session.getAttribute("bank")).thenReturn(bank);
        List<RevenueDTO> revenueData = new ArrayList<>();
        when(paymentService.getMonthlyRevenueByBank(bank.getBankId())).thenReturn(revenueData);

        ResponseEntity<List<RevenueDTO>> response = bankController.getMonthlyRevenue(session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(revenueData, response.getBody());
    }

    @Test
    void testGetMonthlyRevenue_NoBankInSession() {
        when(session.getAttribute("bank")).thenReturn(null);

        ResponseEntity<List<RevenueDTO>> response = bankController.getMonthlyRevenue(session);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
