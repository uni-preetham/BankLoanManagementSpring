package com.crimsonlogic.bankloanmanagementsystem.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class BankTest {

    private Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        bank.setBankName("Crimson Bank");
        bank.setBranchAddress("123 Bank St, City");
        bank.setContactNumber("123-456-7890");
        bank.setLoanTypes(Collections.emptyList()); // Initialize with an empty list
    }

    @Test
    void generateId_ShouldSetBankId_WhenBankIsPersisted() {
        bank.generateId(); // Simulate the @PrePersist behavior
        assertNotNull(bank.getBankId());
        assertTrue(bank.getBankId().startsWith("BAN")); // Check that ID starts with 'BAN'
    }

    @Test
    void bankAttributes_ShouldSetValuesCorrectly() {
        bank.generateId(); // Simulate ID generation

        assertNotNull(bank.getBankId()); // Check that ID is set
        assertEquals("Crimson Bank", bank.getBankName());
        assertEquals("123 Bank St, City", bank.getBranchAddress());
        assertEquals("123-456-7890", bank.getContactNumber());
        assertNotNull(bank.getLoanTypes()); // Check that loanTypes is initialized
        assertTrue(bank.getLoanTypes().isEmpty()); // Check that loanTypes is an empty list
    }

    @Test
    void toString_ShouldReturnCorrectString() {
        bank.generateId(); // Simulate ID generation
        String expectedString = "Bank{" +
                "bankId='" + bank.getBankId() + '\'' +
                ", bankName='Crimson Bank'" +
                ", branchAddress='123 Bank St, City'" +
                ", contactNumber='123-456-7890'" +
                '}';
        assertEquals(expectedString, bank.toString()); // Validate the toString output
    }
}
