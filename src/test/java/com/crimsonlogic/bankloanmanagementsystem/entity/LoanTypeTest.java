package com.crimsonlogic.bankloanmanagementsystem.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoanTypeTest {

    private LoanType loanType;

    @BeforeEach
    void setUp() {
        loanType = new LoanType();
        loanType.setLoanName("Personal Loan");
        loanType.setInterestRate(10.5);
        loanType.setMaxAmount(50000.0);
        loanType.setDurationYears(5);
    }

    @Test
    void generateId_ShouldSetLoanTypeId_WhenLoanTypeIsPersisted() {
        loanType.generateId(); // Simulate the @PrePersist behavior
        assertNotNull(loanType.getLoanTypeId());
        assertEquals("LT", loanType.getLoanTypeId().substring(0, 2)); // Check that ID starts with 'LT'
    }

    @Test
    void loanTypeAttributes_ShouldSetValuesCorrectly() {
        loanType.generateId(); // Simulate ID generation

        assertEquals("Personal Loan", loanType.getLoanName());
        assertEquals(10.5, loanType.getInterestRate());
        assertEquals(50000.0, loanType.getMaxAmount());
        assertEquals(5, loanType.getDurationYears());
    }
}
