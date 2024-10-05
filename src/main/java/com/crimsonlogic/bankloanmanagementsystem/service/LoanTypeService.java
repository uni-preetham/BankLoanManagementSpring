package com.crimsonlogic.bankloanmanagementsystem.service;

import com.crimsonlogic.bankloanmanagementsystem.dto.LoanTypeDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.Bank;
import com.crimsonlogic.bankloanmanagementsystem.entity.LoanType;

public interface LoanTypeService {
    void addLoanType(LoanTypeDTO loanTypeDTO, Bank bank);

	LoanType getLoanTypeById(String loanId);
}
