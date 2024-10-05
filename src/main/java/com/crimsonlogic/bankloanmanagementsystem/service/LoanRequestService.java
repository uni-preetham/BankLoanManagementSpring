package com.crimsonlogic.bankloanmanagementsystem.service;


import java.util.List;


import com.crimsonlogic.bankloanmanagementsystem.dto.LoanRequestDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.LoanRequest;
import com.crimsonlogic.bankloanmanagementsystem.entity.Manager;

public interface LoanRequestService {


	LoanRequest createLoanRequest(LoanRequest loanRequest);

	LoanRequest getLoanRequestById(String loanRequestId);
	

	LoanRequest verifyLoanRequest(String requestId, String status);

	LoanRequest approveLoanRequest(String requestId, String status);

	LoanRequest updateLoanRequest(LoanRequest loanRequest);

	public List<LoanRequestDTO> getAllLoanRequests();

	List<LoanRequestDTO> getAllLoanRequestsForManager(String managerId);

	void updateLoanRequestStatus(String requestId, String status);

	List<LoanRequestDTO> getVerifiedLoanRequestsForBank(String bankId);


	void updateLoanRequestStatus(String requestId, String status, Manager manager);

	List<LoanRequestDTO> getLoanRequestsByUserId(String userId);


}
