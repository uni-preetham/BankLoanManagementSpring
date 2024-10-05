package com.crimsonlogic.bankloanmanagementsystem.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class BankResponseDto {
    private String bankId;
    private String bankName;
    private String branchAddress;
    private String contactNumber;
    private List<LoanTypeResponseDto> loanTypes;

    // Getters and Setters
}
