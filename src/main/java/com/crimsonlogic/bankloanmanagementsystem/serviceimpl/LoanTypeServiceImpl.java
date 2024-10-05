package com.crimsonlogic.bankloanmanagementsystem.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.crimsonlogic.bankloanmanagementsystem.dto.LoanTypeDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.Bank;
import com.crimsonlogic.bankloanmanagementsystem.entity.LoanType;
import com.crimsonlogic.bankloanmanagementsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.bankloanmanagementsystem.repository.LoanTypeRepository;
import com.crimsonlogic.bankloanmanagementsystem.service.LoanTypeService;

@Service
public class LoanTypeServiceImpl implements LoanTypeService {

    @Autowired
    private LoanTypeRepository loanTypeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void addLoanType(LoanTypeDTO loanTypeDTO, Bank bank) {
        LoanType loanType = modelMapper.map(loanTypeDTO, LoanType.class); // Map DTO to Entity
        loanType.setBank(bank);  // Set bank for the loan
        loanTypeRepository.save(loanType);
    }
    
    @Override
    public LoanType getLoanTypeById(String loanId) {
    	return loanTypeRepository.findById(loanId).orElseThrow(()->new ResourceNotFoundException("Loan type with id " + loanId+ " not found"));
    }
}
