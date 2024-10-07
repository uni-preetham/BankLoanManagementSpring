package com.crimsonlogic.bankloanmanagementsystem.repository;

import com.crimsonlogic.bankloanmanagementsystem.entity.Account;
import com.crimsonlogic.bankloanmanagementsystem.entity.PaymentDetail;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {
	
	 @Query("SELECT a FROM Account a WHERE a.bank.bankId = ?1 AND a.approvalDate >= ?2 AND a.approvalDate < ?3")
	    List<Account> findByBankIdAndApprovalDateBetween(String bankId, LocalDate startDate, LocalDate endDate);
	 
}
