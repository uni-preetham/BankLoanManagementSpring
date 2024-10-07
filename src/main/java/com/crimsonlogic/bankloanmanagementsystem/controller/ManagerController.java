package com.crimsonlogic.bankloanmanagementsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crimsonlogic.bankloanmanagementsystem.dto.LoanRequestDTO;
import com.crimsonlogic.bankloanmanagementsystem.dto.ManagerDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.LoanRequest;
import com.crimsonlogic.bankloanmanagementsystem.entity.Manager;
import com.crimsonlogic.bankloanmanagementsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.bankloanmanagementsystem.service.LoanRequestService;
import com.crimsonlogic.bankloanmanagementsystem.service.ManagerService;

import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = "http://localhost:3000/", allowCredentials = "true", allowedHeaders = "*") // adjust the React frontend URL
@RestController
@RequestMapping("/api/manager")
public class ManagerController {


    @Autowired
    private LoanRequestService loanRequestService;
    
    @Autowired
    private ManagerService managerService;
    
    @PutMapping("/editmanager/{managerId}")
    public ResponseEntity<Manager> updateManagerProfile(@PathVariable("managerId") String managerId, @RequestBody ManagerDTO managerDTO, HttpSession session) {
        Manager manager = managerService.updateManagerProfile(managerId, managerDTO);
        session.setAttribute("manager", manager);
        return ResponseEntity.ok(manager); // Return the updated manager object
    }
    
    

	@GetMapping("/details")
	public ResponseEntity<Manager> getManagerDetails(HttpSession session) {
		// Retrieve the user object from session
		Manager manager = (Manager) session.getAttribute("manager");
		if (manager != null) {
			return ResponseEntity.ok(manager); // Return the user details
		} else {
			return ResponseEntity.status(401).build(); // Unauthorized if no user in session
		}
	}

    
    @GetMapping("/loan-requests")
    public ResponseEntity<List<LoanRequestDTO>> getLoanRequestsForManager(HttpSession session) {
        Manager manager = (Manager) session.getAttribute("manager"); // Get manager ID from session
        String managerId = manager.getManagerId();
        List<LoanRequestDTO> loanRequests = loanRequestService.getAllLoanRequestsForManager(managerId);
        return new ResponseEntity<>(loanRequests, HttpStatus.OK);
    }
    
    
	@PutMapping("/verify/{loanRequestId}")
	public ResponseEntity<String> verifyLoanRequest(@PathVariable String loanRequestId, @RequestParam("status") String status) {
	    try {
	        LoanRequest updatedLoanRequest = loanRequestService.verifyLoanRequest(loanRequestId, status);
	        return new ResponseEntity<>("Loan request " + updatedLoanRequest.getStatus().toLowerCase() + " by manager", HttpStatus.OK);
	    } catch (ResourceNotFoundException ex) {
	        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	    } catch (Exception ex) {
	        return new ResponseEntity<>("Error verifying loan request: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	

    @PutMapping("/loan-requests/approve/{requestId}")
    public ResponseEntity<String> approveLoanRequest(@PathVariable String requestId, HttpSession session) {
    	Manager manager = (Manager) session.getAttribute("manager");
        loanRequestService.updateLoanRequestStatus(requestId, "Verified", manager);
        return ResponseEntity.ok("Loan request approved");
    }

    @PutMapping("/loan-requests/reject/{requestId}")
    public ResponseEntity<String> rejectLoanRequest(@PathVariable String requestId, HttpSession session) {
    	Manager manager = (Manager) session.getAttribute("manager");
        loanRequestService.updateLoanRequestStatus(requestId, "Rejected", manager);
        return ResponseEntity.ok("Loan request rejected");
    }

    
    // Fetch all managers
    @GetMapping("/getallmanagers")
    public ResponseEntity<List<Manager>> getAllManagers() {
        List<Manager> managers = managerService.getAllManagers();
        if (managers.isEmpty()) {
            return ResponseEntity.noContent().build();  // Return 204 No Content if no managers found
        }
        return ResponseEntity.ok(managers);  // Return the list of managers with 200 OK
    }

    // Delete (disable) a manager by ID
    @DeleteMapping("deletemanager/{managerId}")
    public ResponseEntity<Void> deleteManagerById(@PathVariable String managerId) {
        try {
            managerService.deleteManagerById(managerId);  // Call the service to delete the manager
            return ResponseEntity.noContent().build();    // Return 204 No Content after deletion
        } catch (Exception e) {
            return ResponseEntity.notFound().build();     // Return 404 Not Found if the manager ID does not exist
        }
    }

}
