package com.crimsonlogic.bankloanmanagementsystem.serviceimpl;

import com.crimsonlogic.bankloanmanagementsystem.entity.LoanRequest;
import com.crimsonlogic.bankloanmanagementsystem.entity.LoanType;
import com.crimsonlogic.bankloanmanagementsystem.entity.Manager;
import com.crimsonlogic.bankloanmanagementsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.bankloanmanagementsystem.repository.LoanRequestRepository;
import com.crimsonlogic.bankloanmanagementsystem.repository.ManagerRepository;
import com.crimsonlogic.bankloanmanagementsystem.repository.PaymentDetailRepository;
import com.crimsonlogic.bankloanmanagementsystem.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoanRequestServiceImplTest {

    @InjectMocks
    private LoanRequestServiceImpl loanRequestService;

    @Mock
    private LoanRequestRepository loanRequestRepository;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private PaymentDetailRepository paymentDetailRepository;

    @Mock
    private AccountService accountService;

    private LoanRequest loanRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loanRequest = new LoanRequest();
        loanRequest.setRequestId("LOAN123");
        loanRequest.setRequestedAmount(10000.0);
        loanRequest.setStatus("Pending");
        loanRequest.setApprovalDate(null);
    }

    @Test
    void createLoanRequest_ShouldSaveLoanRequest() {
        when(loanRequestRepository.save(any(LoanRequest.class))).thenReturn(loanRequest);

        LoanRequest createdLoanRequest = loanRequestService.createLoanRequest(loanRequest);

        assertNotNull(createdLoanRequest);
        assertEquals("LOAN123", createdLoanRequest.getRequestId());
        verify(loanRequestRepository, times(1)).save(loanRequest);
    }

    @Test
    void getLoanRequestById_ShouldReturnLoanRequest_WhenExists() {
        when(loanRequestRepository.findById("LOAN123")).thenReturn(Optional.of(loanRequest));

        LoanRequest fetchedLoanRequest = loanRequestService.getLoanRequestById("LOAN123");

        assertNotNull(fetchedLoanRequest);
        assertEquals("LOAN123", fetchedLoanRequest.getRequestId());
        verify(loanRequestRepository, times(1)).findById("LOAN123");
    }

    @Test
    void getLoanRequestById_ShouldThrowException_WhenNotFound() {
        when(loanRequestRepository.findById("LOAN999")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
            () -> loanRequestService.getLoanRequestById("LOAN999"));

        assertEquals("Loan request not found with ID: LOAN999", exception.getMessage());
    }

    @Test
    void approveLoanRequest_ShouldUpdateLoanRequestToApproved() {
        // Create a mock LoanType with a valid interest rate
        LoanType loanType = new LoanType();
        loanType.setInterestRate(5.0); // Set an example interest rate
        loanType.setDurationYears(15); // Set the loan term (example)

        // Set up the loan request with the mock LoanType
        loanRequest.setLoanType(loanType);
        loanRequest.setRequestedAmount(100000.0); // Set an example requested amount
        loanRequest.setLoanTerm(15); // Set the loan term

        // Mock the repository behavior
        when(loanRequestRepository.findById("LOAN123")).thenReturn(Optional.of(loanRequest));
        when(loanRequestRepository.save(any(LoanRequest.class))).thenReturn(loanRequest);

        // Call the method under test
        LoanRequest approvedLoanRequest = loanRequestService.approveLoanRequest("LOAN123", "Approved");

        // Verify the results
        assertEquals("Approved", approvedLoanRequest.getStatus());
        verify(loanRequestRepository, times(1)).save(loanRequest);
    }


    @Test
    void approveLoanRequest_ShouldThrowException_WhenNotFound() {
        when(loanRequestRepository.findById("LOAN999")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
            () -> loanRequestService.approveLoanRequest("LOAN999", "Approved"));

        assertEquals("Request id not found", exception.getMessage());
    }

    @Test
    void verifyLoanRequest_ShouldUpdateLoanRequestToVerified() {
        loanRequest.setStatus("Pending");
        when(loanRequestRepository.findById("LOAN123")).thenReturn(Optional.of(loanRequest));
        when(loanRequestRepository.save(any(LoanRequest.class))).thenReturn(loanRequest);

        LoanRequest verifiedLoanRequest = loanRequestService.verifyLoanRequest("LOAN123", "Verified");

        assertEquals("Verified", verifiedLoanRequest.getStatus());
        verify(loanRequestRepository, times(1)).save(loanRequest);
    }

    @Test
    void verifyLoanRequest_ShouldThrowException_WhenNotFound() {
        when(loanRequestRepository.findById("LOAN999")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
            () -> loanRequestService.verifyLoanRequest("LOAN999", "Verified"));

        assertEquals("Request id is null.", exception.getMessage());
    }
}
