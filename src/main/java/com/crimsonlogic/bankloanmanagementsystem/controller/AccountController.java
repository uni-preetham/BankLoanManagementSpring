package com.crimsonlogic.bankloanmanagementsystem.controller;

import com.crimsonlogic.bankloanmanagementsystem.entity.Account;
import com.crimsonlogic.bankloanmanagementsystem.entity.Address;
import com.crimsonlogic.bankloanmanagementsystem.entity.User;
import com.crimsonlogic.bankloanmanagementsystem.service.AccountService;
import com.crimsonlogic.bankloanmanagementsystem.service.AddressService;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
