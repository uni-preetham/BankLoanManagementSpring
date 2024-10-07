package com.crimsonlogic.bankloanmanagementsystem.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crimsonlogic.bankloanmanagementsystem.entity.Account;
import com.crimsonlogic.bankloanmanagementsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.bankloanmanagementsystem.repository.AccountRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setAccountId("ACC123");
        account.setTotalPayment(5000.0);
        account.setApprovalDate(LocalDate.now());
    }

    @Test
    void createAccount_ShouldSaveAccount_WithCurrentDate() {
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account savedAccount = accountService.createAccount(account);

        assertEquals(LocalDate.now(), savedAccount.getApprovalDate());
        assertEquals("ACC123", savedAccount.getAccountId());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void getAccountsByUser_ShouldReturnAccounts_WhenAccountsExist() {
        Account account1 = new Account();
        account1.setAccountId("ACC124");
        Account account2 = new Account();
        account2.setAccountId("ACC125");

        when(accountRepository.findByUserUserId(anyString())).thenReturn(Arrays.asList(account1, account2));

        List<Account> accounts = accountService.getAccountsByUser("USER123");

        assertEquals(2, accounts.size());
        assertEquals("ACC124", accounts.get(0).getAccountId());
        assertEquals("ACC125", accounts.get(1).getAccountId());
        verify(accountRepository, times(1)).findByUserUserId("USER123");
    }

    @Test
    void getAccountsByUser_ShouldReturnEmptyList_WhenNoAccountsExist() {
        when(accountRepository.findByUserUserId(anyString())).thenReturn(Arrays.asList());

        List<Account> accounts = accountService.getAccountsByUser("USER123");

        assertEquals(0, accounts.size());
        verify(accountRepository, times(1)).findByUserUserId("USER123");
    }

    @Test
    void getAccountById_ShouldReturnAccount_WhenAccountExists() {
        when(accountRepository.findById(anyString())).thenReturn(Optional.of(account));

        Account foundAccount = accountService.getAccountById("ACC123");

        assertEquals("ACC123", foundAccount.getAccountId());
        verify(accountRepository, times(1)).findById("ACC123");
    }

    @Test
    void getAccountById_ShouldThrowException_WhenAccountDoesNotExist() {
        when(accountRepository.findById(anyString())).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.getAccountById("ACC123");
        });

        assertEquals("AccountId not found", thrown.getMessage());
        verify(accountRepository, times(1)).findById("ACC123");
    }
}
