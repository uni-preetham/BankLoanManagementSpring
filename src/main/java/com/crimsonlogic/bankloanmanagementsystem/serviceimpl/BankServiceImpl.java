package com.crimsonlogic.bankloanmanagementsystem.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crimsonlogic.bankloanmanagementsystem.dto.BankResponseDto;
import com.crimsonlogic.bankloanmanagementsystem.dto.LoanTypeResponseDto;
import com.crimsonlogic.bankloanmanagementsystem.entity.Bank;
import com.crimsonlogic.bankloanmanagementsystem.repository.BankRepository;
import com.crimsonlogic.bankloanmanagementsystem.service.BankService;

@Service
public class BankServiceImpl implements BankService {

	
	@Autowired
	private BankRepository bankRepository;
	
	@Override
    public Bank findByLoginId(String loginId) {
        return bankRepository.findByLogin_LoginId(loginId);
    }
	
	
	 public List<BankResponseDto> findAllBanksWithLoans() {
	        List<Bank> banks = bankRepository.findAll(); // Fetch all banks from the database

	        return banks.stream().map(bank -> {
	            // Create a simplified response object
	            BankResponseDto response = new BankResponseDto();
	            response.setBankId(bank.getBankId());
	            response.setBankName(bank.getBankName());
	            response.setBranchAddress(bank.getBranchAddress());
	            response.setContactNumber(bank.getContactNumber());

	            // Map loan types to a simplified structure
	            List<LoanTypeResponseDto> loanTypes = bank.getLoanTypes().stream().map(loanType -> {
	                LoanTypeResponseDto loanTypeDto = new LoanTypeResponseDto();
	                loanTypeDto.setLoanTypeId(loanType.getLoanTypeId());
	                loanTypeDto.setLoanName(loanType.getLoanName());
	                loanTypeDto.setInterestRate(loanType.getInterestRate());
	                loanTypeDto.setMaxAmount(loanType.getMaxAmount());
	                loanTypeDto.setDurationYears(loanType.getDurationYears());
	                return loanTypeDto;
	            }).collect(Collectors.toList());

	            response.setLoanTypes(loanTypes);
	            return response;
	        }).collect(Collectors.toList());
	    }
}
