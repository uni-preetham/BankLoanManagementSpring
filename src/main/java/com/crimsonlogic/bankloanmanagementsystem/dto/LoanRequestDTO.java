package com.crimsonlogic.bankloanmanagementsystem.dto;

import java.time.LocalDate;

import com.crimsonlogic.bankloanmanagementsystem.entity.LoanType;
import com.crimsonlogic.bankloanmanagementsystem.entity.Manager;
import com.crimsonlogic.bankloanmanagementsystem.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequestDTO {
    private String requestId;
    private User user; // Adjust based on your User entity
    private Manager manager;
    private LoanType loanType; // Adjust based on your LoanType entity
    private Double requestedAmount;
    private String status;
    private LocalDate requestDate;
    private String documentPath;


}
