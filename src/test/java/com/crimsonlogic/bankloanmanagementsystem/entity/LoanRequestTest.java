package com.crimsonlogic.bankloanmanagementsystem.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoanRequestTest {

    private LoanRequest loanRequest;

    @BeforeEach
    void setUp() {
        loanRequest = new LoanRequest();
        loanRequest.setRequestedAmount(25000.0);
        loanRequest.setLoanTerm(5); // Loan term in years
        loanRequest.setUser(new User()); // Assume User is a valid entity
        loanRequest.setLoanType(new LoanType()); // Assume LoanType is a valid entity
    }

    @Test
    void generateId_ShouldSetRequestId_WhenLoanRequestIsPersisted() {
        loanRequest.generateId(); // Simulate the @PrePersist behavior
        assertNotNull(loanRequest.getRequestId());
        assertEquals("REQ", loanRequest.getRequestId().substring(0, 3)); // Check that ID starts with 'REQ'
    }

    @Test
    void loanRequestAttributes_ShouldSetValuesCorrectly() {
        loanRequest.generateId(); // Simulate ID generation

        assertNotNull(loanRequest.getRequestId()); // Check that ID is set
        assertEquals(25000.0, loanRequest.getRequestedAmount());
        assertEquals(5, loanRequest.getLoanTerm());
        assertNotNull(loanRequest.getRequestDate()); // Check that request date is set
        assertEquals("Pending", loanRequest.getStatus()); // Default status
        assertNotNull(loanRequest.getUser()); // Check that user is set
        assertNotNull(loanRequest.getLoanType()); // Check that loan type is set
        assertEquals(false, loanRequest.getIsEligible()); // Default eligibility
    }
}
