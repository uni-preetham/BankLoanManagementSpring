package com.crimsonlogic.bankloanmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.bankloanmanagementsystem.entity.Bank;

public interface BankRepository extends JpaRepository<Bank, String>{
	Bank findByLogin_LoginId(String loginId); // Find user by loginId

}
