package com.crimsonlogic.bankloanmanagementsystem.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crimsonlogic.bankloanmanagementsystem.entity.Account;
import com.crimsonlogic.bankloanmanagementsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.bankloanmanagementsystem.repository.AccountRepository;
import com.crimsonlogic.bankloanmanagementsystem.service.AccountService;

import java.time.LocalDate;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountRepository accountRepository;
    

    @Override
    public Account createAccount(Account account) {
        account.setApprovalDate(LocalDate.now());
        return accountRepository.save(account);
    }
    
    @Override
    public List<Account> getAccountsByUser(String userId) {
        return accountRepository.findByUserUserId(userId);
    }
    
    @Override
    public Account getAccountById(String accountId) {
    	return accountRepository.findById(accountId).orElseThrow(()->new ResourceNotFoundException("AccountId not found"));
    }
  
}
