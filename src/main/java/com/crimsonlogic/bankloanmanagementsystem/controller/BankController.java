package com.crimsonlogic.bankloanmanagementsystem.controller;

import com.crimsonlogic.bankloanmanagementsystem.dto.BankResponseDto;
import com.crimsonlogic.bankloanmanagementsystem.dto.LoanRequestDTO;
import com.crimsonlogic.bankloanmanagementsystem.dto.RevenueDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.Bank;
import com.crimsonlogic.bankloanmanagementsystem.entity.LoanRequest;
import com.crimsonlogic.bankloanmanagementsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.bankloanmanagementsystem.service.BankService;
import com.crimsonlogic.bankloanmanagementsystem.service.LoanRequestService;
import com.crimsonlogic.bankloanmanagementsystem.service.PaymentDetailService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000/", allowCredentials = "true")
@RestController
@RequestMapping("/api/banks")
public class BankController {

    @Autowired
    private BankService bankService;

    @Autowired
    private LoanRequestService loanRequestService;

    @Autowired
    private PaymentDetailService paymentService;

    @GetMapping("/with-loans")
    public List<BankResponseDto> getBanksWithLoans() {
        return bankService.findAllBanksWithLoans(); // Call the modified service method
    }
    
    
    @GetMapping("/details")
	public ResponseEntity<Bank> getBankDetails(HttpSession session) {
		// Retrieve the user object from session
		Bank bank = (Bank) session.getAttribute("bank");
//		System.out.println(bank);
		if (bank != null) {
			return ResponseEntity.ok(bank); // Return the user details
		} else {
			return ResponseEntity.status(401).build(); // Unauthorized if no user in session
		}
	}
    
    @PutMapping("/approve/{loanRequestId}")
    public ResponseEntity<String> approveLoanRequest(@PathVariable String loanRequestId, @RequestParam("status") String status) {
    	LoanRequest updatedLoanRequest = loanRequestService.approveLoanRequest(loanRequestId, status);
    	return new ResponseEntity<>("Loan request " + updatedLoanRequest.getStatus().toLowerCase() + " by bank", HttpStatus.OK);
//        try {
//            System.err.println("Called");
//        } catch (ResourceNotFoundException ex) {
//        	System.err.println("Called2");
//            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
//        } catch (Exception ex) {
//        	System.err.println("Called3");
//            return new ResponseEntity<>("Error approving loan request: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }

    
    @GetMapping("/verified-loan-requests")
    public ResponseEntity<List<LoanRequestDTO>> getVerifiedLoanRequests(HttpSession session) {
		Bank bank = (Bank) session.getAttribute("bank"); // Get bank ID from session
        List<LoanRequestDTO> verifiedRequests = loanRequestService.getVerifiedLoanRequestsForBank(bank.getBankId());
        return new ResponseEntity<>(verifiedRequests, HttpStatus.OK);
    }

    @PutMapping("/update-loan-status/{requestId}")
    public ResponseEntity<String> updateLoanStatus(@PathVariable String requestId, @RequestParam String status) {
        loanRequestService.updateLoanRequestStatus(requestId, status);
        return new ResponseEntity<>("Loan request status updated successfully", HttpStatus.OK);
    }
    
    @GetMapping("/monthly")
    public ResponseEntity<List<RevenueDTO>> getMonthlyRevenue(HttpSession session) {
        Bank bank = (Bank) session.getAttribute("bank");
        if (bank == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        
        // Fetch revenue data specific to the bank
        List<RevenueDTO> revenueData = paymentService.getMonthlyRevenueByBank(bank.getBankId());
        System.err.println("Controller trig");
        return ResponseEntity.ok(revenueData);
    }
    
}
