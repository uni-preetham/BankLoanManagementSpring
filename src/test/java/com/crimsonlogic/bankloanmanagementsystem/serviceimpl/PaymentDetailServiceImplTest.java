package com.crimsonlogic.bankloanmanagementsystem.serviceimpl;

import com.crimsonlogic.bankloanmanagementsystem.dto.RevenueDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.Account;
import com.crimsonlogic.bankloanmanagementsystem.entity.PaymentDetail;
import com.crimsonlogic.bankloanmanagementsystem.repository.AccountRepository;
import com.crimsonlogic.bankloanmanagementsystem.repository.PaymentDetailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class PaymentDetailServiceImplTest {

    @InjectMocks
    private PaymentDetailServiceImpl paymentDetailService;

    @Mock
    private PaymentDetailRepository paymentDetailRepository;

    @Mock
    private AccountRepository accountRepository;

    private List<Account> accounts;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accounts = new ArrayList<>();
    }

    @Test
    void testGetMonthlyRevenueByBank_Success() {
        // Prepare test accounts
        Account account1 = new Account();
        account1.setTotalPayment(500.0);
        account1.setApprovalDate(LocalDate.of(2024, 10, 1)); // October 2024

        Account account2 = new Account();
        account2.setTotalPayment(300.0);
        account2.setApprovalDate(LocalDate.of(2024, 10, 15)); // October 2024

        accounts.add(account1);
        accounts.add(account2);

        // Mocking the repository response
        when(accountRepository.findByBankBankId(anyString())).thenReturn(accounts);

        // Call the method to test
        List<RevenueDTO> revenueList = paymentDetailService.getMonthlyRevenueByBank("BANK1");

        // Assertions
        assertNotNull(revenueList);
        assertEquals(12, revenueList.size()); // 12 months should be present

        // Check October's revenue (month 10)
        assertEquals(10, revenueList.get(9).getMonth()); // Months are 1-based in RevenueDTO
        assertEquals(800.0, revenueList.get(9).getTotalRevenue(), 0.01); // 500 + 300 = 800

        // Check revenues for other months should be zero
        for (int month = 0; month < 12; month++) {
            if (month != 9) { // October
                assertEquals(0.0, revenueList.get(month).getTotalRevenue(), 0.01);
            }
        }
    }

    @Test
    void testGetMonthlyRevenueByBank_NoAccounts() {
        // Mocking an empty account list for the bank
        when(accountRepository.findByBankBankId(anyString())).thenReturn(new ArrayList<>());

        // Call the method to test
        List<RevenueDTO> revenueList = paymentDetailService.getMonthlyRevenueByBank("BANK2");

        // Assertions
        assertNotNull(revenueList);
        assertEquals(12, revenueList.size()); // Still expect 12 months to be present

        // Check all months' revenue should be zero
        for (RevenueDTO revenue : revenueList) {
            assertEquals(0.0, revenue.getTotalRevenue(), 0.01);
        }
    }
}
