package com.crimsonlogic.bankloanmanagementsystem.service;

import java.util.List;

import com.crimsonlogic.bankloanmanagementsystem.dto.LoanTypeDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.Bank;
import com.crimsonlogic.bankloanmanagementsystem.entity.LoanType;

public interface LoanTypeService {
    void addLoanType(LoanTypeDTO loanTypeDTO, Bank bank);

	LoanType getLoanTypeById(String loanId);
	
	 List<LoanType> getLoanTypesByBank(Bank bank);

	void deleteLoanTypeById(String loanTypeId);

	void editLoanType(String loanTypeId, LoanTypeDTO loanTypeDTO);
}
