package com.crimsonlogic.bankloanmanagementsystem.controller;

import com.crimsonlogic.bankloanmanagementsystem.dto.LoanTypeDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.Bank;
import com.crimsonlogic.bankloanmanagementsystem.entity.LoanType;
import com.crimsonlogic.bankloanmanagementsystem.service.LoanTypeService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "http://localhost:3000/", allowCredentials = "true")
public class LoanTypeController {

    @Autowired
    private LoanTypeService loanTypeService;

    @PostMapping("/add")
    public ResponseEntity<String> addLoanType(@RequestBody LoanTypeDTO loanTypeDTO, HttpSession session) {
        Bank bank = (Bank) session.getAttribute("bank"); // Fetch bank from session

        if (bank != null) {
            loanTypeService.addLoanType(loanTypeDTO, bank);
            return ResponseEntity.ok("Loan details added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bank not found in session");
        }
    }
    
    
    @GetMapping("/{loanTypeId}")
    public ResponseEntity<LoanType> getLoanTypeById(@PathVariable String loanTypeId) {
        LoanType loanType = loanTypeService.getLoanTypeById(loanTypeId);
        if (loanType != null) {
            return ResponseEntity.ok(loanType);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
