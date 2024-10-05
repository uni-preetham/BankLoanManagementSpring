package com.crimsonlogic.bankloanmanagementsystem.repository;

import com.crimsonlogic.bankloanmanagementsystem.entity.LoanRequest;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRequestRepository extends JpaRepository<LoanRequest, String> {
	public List<LoanRequest> findByStatus(String status);

	List<LoanRequest> findByLoanTypeBankBankId(String bankId);
	  List<LoanRequest> findByUser_UserId(String userId);
//	 List<LoanRequest> findByLoanType_Bank_BankIdAndStatus(String bankId, String status);
	 List<LoanRequest> findByLoanType_Bank_BankId(String bankId);
}
