package com.crimsonlogic.bankloanmanagementsystem.service;

import java.util.List;

import com.crimsonlogic.bankloanmanagementsystem.entity.Account;

public interface AccountService {

	Account createAccount(Account account);

	List<Account> getAccountsByUser(String userId);

	Account getAccountById(String accountId);

//	List<Account> getAccountsByUserId(String userId);

}
