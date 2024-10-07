package com.crimsonlogic.bankloanmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.bankloanmanagementsystem.entity.Account;
import com.crimsonlogic.bankloanmanagementsystem.entity.User;

public interface AccountRepository extends JpaRepository<Account, String> {
	List<Account> findByUser(User user);
	List<Account> findByUserUserId(String userId);

	 List<Account> findByBankBankId(String bankId);
}
