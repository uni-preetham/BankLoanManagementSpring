package com.crimsonlogic.bankloanmanagementsystem.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class AccountTest {

    private Account account;
    private User user; // Simulate User entity
    private Bank bank; // Simulate Bank entity
    private LoanType loanType; // Simulate LoanType entity

    @BeforeEach
    void setUp() {
        user = new User(); // Initialize a User object
        bank = new Bank(); // Initialize a Bank object
        loanType = new LoanType(); // Initialize a LoanType object
        
        // Set up sample values for the Account
        account = new Account(user, bank, loanType, 10000.0, LocalDate.now(), 5.0, 10);
    }

    @Test
    void generateId_ShouldSetAccountId_WhenAccountIsPersisted() {
        account.generateId(); // Simulate the @PrePersist behavior
        assertNotNull(account.getAccountId());
        assertTrue(account.getAccountId().startsWith("ACC")); // Check that ID starts with 'ACC'
    }

    @Test
    void accountAttributes_ShouldSetValuesCorrectly() {
        account.generateId(); // Simulate ID generation

        assertNotNull(account.getAccountId()); // Check that ID is set
        assertEquals(user, account.getUser());
        assertEquals(bank, account.getBank());
        assertEquals(loanType, account.getLoanType());
        assertEquals(10000.0, account.getApprovedAmount());
        assertEquals(5.0, account.getInterestRate());
        assertEquals(10, account.getDurationYears());
        assertNotNull(account.getApprovalDate()); // Ensure approval date is set
    }

    @Test
    void constructor_ShouldInitializeAllFieldsCorrectly() {
        // Validate that all fields are initialized through the constructor
        assertNotNull(account.getUser());
        assertNotNull(account.getBank());
        assertNotNull(account.getLoanType());
        assertEquals(10000.0, account.getApprovedAmount());
        assertNotNull(account.getApprovalDate());
        assertEquals(5.0, account.getInterestRate());
        assertEquals(10, account.getDurationYears());
    }

}
