package com.crimsonlogic.bankloanmanagementsystem.controller;

import com.crimsonlogic.bankloanmanagementsystem.dto.ManagerRegistrationDTO;
import com.crimsonlogic.bankloanmanagementsystem.dto.UserRegistrationDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.Bank;
import com.crimsonlogic.bankloanmanagementsystem.exception.UserAlreadyExistsException;
import com.crimsonlogic.bankloanmanagementsystem.service.ManagerService;
import com.crimsonlogic.bankloanmanagementsystem.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
@CrossOrigin(origins = "http://localhost:3000/", allowCredentials = "true") // adjust the React frontend URL
public class RegistrationController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private ManagerService managerService;
    

    @PostMapping("/registeruser")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
    	try {
        userService.registerUser(registrationDTO);
        return ResponseEntity.ok("User registered successfully");
    	} catch (UserAlreadyExistsException ex) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    	}
    }
    
 

    @PostMapping("/registermanager")
    public ResponseEntity<String> registerManager(@RequestBody ManagerRegistrationDTO managerDTO, HttpSession session) {
        Bank bank = (Bank) session.getAttribute("bank"); // Get bank from session
        System.out.println(bank);
        if (bank != null) {
            managerService.registerManager(managerDTO, bank); // Pass bank to the service
            return ResponseEntity.ok("Manager registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bank not found in session");
        }
    }
    
}
