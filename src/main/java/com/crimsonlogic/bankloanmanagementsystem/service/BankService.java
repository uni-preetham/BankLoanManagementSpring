package com.crimsonlogic.bankloanmanagementsystem.service;

import java.util.List;

import com.crimsonlogic.bankloanmanagementsystem.dto.BankResponseDto;
import com.crimsonlogic.bankloanmanagementsystem.entity.Bank;

public interface BankService {

	List<BankResponseDto> findAllBanksWithLoans();

	Bank findByLoginId(String loginId);

}
