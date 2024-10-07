package com.crimsonlogic.bankloanmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.bankloanmanagementsystem.entity.Bank;
import com.crimsonlogic.bankloanmanagementsystem.entity.LoanType;

public interface LoanTypeRepository extends JpaRepository<LoanType, String> {
	List<LoanType> findByBank(Bank bank);
}
