package com.crimsonlogic.bankloanmanagementsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoanTypeDTO {
    private String loanName;
    private Double interestRate;
    private Double maxAmount;
    private Integer durationYears;
}
