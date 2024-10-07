package com.crimsonlogic.bankloanmanagementsystem.controller;

import com.crimsonlogic.bankloanmanagementsystem.entity.Account;
import com.crimsonlogic.bankloanmanagementsystem.entity.User;
import com.crimsonlogic.bankloanmanagementsystem.service.AccountService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    @Mock
    private HttpSession session;

    private User user;
    private List<Account> accounts;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserId("USER1");
        user.setFirstName("John");
        user.setLastName("Doe");

        // Mock account list
        accounts = new ArrayList<>();
        Account account = new Account();
        account.setAccountId("ACC1");
        account.setUser(user);
        account.setTotalPayment(1000.00);
        accounts.add(account);
    }

    @Test
    void testGetAccountsByUser_Success() {
        when(session.getAttribute("user")).thenReturn(user);
        when(accountService.getAccountsByUser(user.getUserId())).thenReturn(accounts);

        List<Account> response = accountController.getAccountsByUser(session);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("ACC1", response.get(0).getAccountId());
        verify(session).getAttribute("user");
        verify(accountService).getAccountsByUser(user.getUserId());
    }

    @Test
    void testGetAccountsByUserId_Found() {
        when(accountService.getAccountsByUser("USER1")).thenReturn(accounts);

        ResponseEntity<List<Account>> response = accountController.getAccountsByUserId("USER1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accounts, response.getBody());
        verify(accountService).getAccountsByUser("USER1");
    }

    @Test
    void testGetAccountsByUserId_NotFound() {
        when(accountService.getAccountsByUser("USER2")).thenReturn(new ArrayList<>());

        ResponseEntity<List<Account>> response = accountController.getAccountsByUserId("USER2");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(accountService).getAccountsByUser("USER2");
    }

    @Test
    void testGetAccountsByAccountId_Found() {
        Account account = new Account();
        account.setAccountId("ACC1");
        account.setUser(user);
        account.setTotalPayment(1000.00);

        when(accountService.getAccountById("ACC1")).thenReturn(account);

        ResponseEntity<Account> response = accountController.getAccountsByAccountId("ACC1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());
        verify(accountService).getAccountById("ACC1");
    }

    @Test
    void testGetAccountsByAccountId_NotFound() {
        when(accountService.getAccountById("ACC2")).thenReturn(null);

        ResponseEntity<Account> response = accountController.getAccountsByAccountId("ACC2");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(accountService).getAccountById("ACC2");
    }
}
