package com.crimsonlogic.bankloanmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.bankloanmanagementsystem.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

	List<Account> findByUserUserId(String userId);
}
