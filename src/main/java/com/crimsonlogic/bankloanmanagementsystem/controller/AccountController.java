package com.crimsonlogic.bankloanmanagementsystem.controller;

import com.crimsonlogic.bankloanmanagementsystem.entity.Account;
import com.crimsonlogic.bankloanmanagementsystem.entity.User;
import com.crimsonlogic.bankloanmanagementsystem.service.AccountService;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * @author Preetham
*/
@CrossOrigin(origins = "http://localhost:3000/", allowCredentials = "true") // adjust the React frontend URL
@RestController
@RequestMapping("/api/account")
public class AccountController {

	@Autowired
    private AccountService accountService;

    // Get all accounts by user ID
    @GetMapping("/accountdetails")
    public List<Account> getAccountsByUser(HttpSession session) {
    	User user= (User) session.getAttribute("user");
        return accountService.getAccountsByUser(user.getUserId());
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUserId(@PathVariable String userId) {
        List<Account> accounts = accountService.getAccountsByUser(userId);

        if (accounts != null && !accounts.isEmpty()) {
            return ResponseEntity.ok(accounts); 
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/account/{accountId}")
    public ResponseEntity<Account> getAccountsByAccountId(@PathVariable String accountId) {
        Account account = accountService.getAccountById(accountId);

        if (account != null) {
            return ResponseEntity.ok(account); // Return the accounts if found
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if no accounts found
        }
    }
}
