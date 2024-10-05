package com.crimsonlogic.bankloanmanagementsystem.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoanTypeResponseDto {
    private String loanTypeId;
    private String loanName;
    private double interestRate;
    private double maxAmount;
    private int durationYears;

    // Getters and Setters
}
