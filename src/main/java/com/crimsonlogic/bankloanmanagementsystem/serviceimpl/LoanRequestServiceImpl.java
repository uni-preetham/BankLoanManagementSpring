package com.crimsonlogic.bankloanmanagementsystem.serviceimpl;

import com.crimsonlogic.bankloanmanagementsystem.dto.LoanRequestDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.Account;
import com.crimsonlogic.bankloanmanagementsystem.entity.LoanRequest;
import com.crimsonlogic.bankloanmanagementsystem.entity.Manager;
import com.crimsonlogic.bankloanmanagementsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.bankloanmanagementsystem.repository.LoanRequestRepository;
import com.crimsonlogic.bankloanmanagementsystem.repository.ManagerRepository;
import com.crimsonlogic.bankloanmanagementsystem.service.AccountService;
import com.crimsonlogic.bankloanmanagementsystem.service.LoanRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoanRequestServiceImpl implements LoanRequestService {

    @Autowired
    private LoanRequestRepository loanRequestRepository;
    
    
    
    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private AccountService accountService;


    @Override
    public LoanRequest createLoanRequest(LoanRequest loanRequest) {
        return loanRequestRepository.save(loanRequest);
    }

    @Override
    public LoanRequest getLoanRequestById(String loanRequestId) {
        return loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found with ID: " + loanRequestId));
    }

    // Verify a loan request and update its status
    @Override
    public LoanRequest verifyLoanRequest(String requestId, String status) {
        LoanRequest loanRequest = loanRequestRepository.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("Request id is null."));
        
        if (status.equals("Verified")) {
            loanRequest.setStatus("Verified");
//            loanRequest.setEligibilityCheckDate(LocalDate.now());
        } else if (status.equals("Rejected")) {
            loanRequest.setStatus("Rejected");
        }
        
        return updateLoanRequest(loanRequest);
    }

    // Approve or reject a loan request
    @Override
    public LoanRequest approveLoanRequest(String requestId, String status) {
//        LoanRequest loanRequest = getLoanRequestById(requestId);
        LoanRequest loanRequest = loanRequestRepository.findById(requestId).orElseThrow(()-> new ResourceNotFoundException("Request id not found"));
    	
        
        if (status.equals("Approved")) {
            loanRequest.setStatus("Approved");
            loanRequest.setApprovalDate(LocalDate.now());
            Account account = new Account(
                    loanRequest.getUser(),
                    loanRequest.getLoanType().getBank(),
                    loanRequest.getLoanType(),
                    loanRequest.getRequestedAmount(),
                    LocalDate.now(), // Approval date
                    loanRequest.getLoanType().getInterestRate(),
                    loanRequest.getLoanType().getDurationYears()
                );

                accountService.createAccount(account); // Save the account details

        } else if (status.equals("Rejected")) {
            loanRequest.setStatus("Rejected");
        }
        
        return updateLoanRequest(loanRequest);
    }

    // Update an existing loan request
    @Override
    public LoanRequest updateLoanRequest(LoanRequest loanRequest) {
        Optional<LoanRequest> existingLoanRequest = loanRequestRepository.findById(loanRequest.getRequestId());

        if (existingLoanRequest.isPresent()) {
            return loanRequestRepository.save(loanRequest);
        } else {
            throw new ResourceNotFoundException("Loan request not found with ID: " + loanRequest.getRequestId());
        }
    }

    // Get all loan requests as DTOs
    @Override
    public List<LoanRequestDTO> getAllLoanRequests() {
        return loanRequestRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get all loan requests for a specific manager by bank
    @Override
    public List<LoanRequestDTO> getAllLoanRequestsForManager(String managerId) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + managerId));

        String bankId = manager.getBank().getBankId();

        List<LoanRequest> loanRequests = loanRequestRepository.findByLoanTypeBankBankId(bankId);
        return loanRequests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get verified loan requests for a specific bank
    @Override
    public List<LoanRequestDTO> getVerifiedLoanRequestsForBank(String bankId) {
//        List<LoanRequest> verifiedRequests = loanRequestRepository.findByLoanType_Bank_BankIdAndStatus(bankId, "Verified");
        List<LoanRequest> verifiedRequests = loanRequestRepository.findByLoanType_Bank_BankId(bankId);
        System.out.println(verifiedRequests);
        return verifiedRequests.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Convert LoanRequest entity to LoanRequestDTO
    private LoanRequestDTO convertToDTO(LoanRequest loanRequest) {
        return new LoanRequestDTO(
                loanRequest.getRequestId(),
                loanRequest.getUser(),
                loanRequest.getManager(),
                loanRequest.getLoanType(),
                loanRequest.getRequestedAmount(),
                loanRequest.getStatus(),
                loanRequest.getRequestDate(),
                loanRequest.getDocumentPath()
        );
    }

    // Update the status of a loan request
    @Override
    public void updateLoanRequestStatus(String requestId, String status) {
        LoanRequest loanRequest = loanRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found with ID: " + requestId));

        loanRequest.setStatus(status);

        if ("Approved".equals(status)) {
            loanRequest.setApprovalDate(LocalDate.now());
        }

        loanRequestRepository.save(loanRequest);
    }
    
    @Override
    public void updateLoanRequestStatus(String requestId, String status, Manager manager) {
        LoanRequest loanRequest = loanRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found with ID: " + requestId));

        loanRequest.setStatus(status);
        loanRequest.setManager(manager);
//        loanRequest.setIsEligible(true);

        if ("Approved".equals(status)) {
            loanRequest.setApprovalDate(LocalDate.now());
        }

        loanRequestRepository.save(loanRequest);
    }
    
    @Override
    public List<LoanRequestDTO> getLoanRequestsByUserId(String userId) {
        return loanRequestRepository.findByUser_UserId(userId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

}
