package com.crimsonlogic.bankloanmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.crimsonlogic.bankloanmanagementsystem.util.IdGenerator;

@Entity
@Data
@NoArgsConstructor
@Table(name = "loan_requests")
public class LoanRequest {

    @Id
    private String requestId;
    
    @PrePersist
    public void generateId() {
        this.requestId = IdGenerator.generateId("REQ");
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "loan_type_id", referencedColumnName = "loanTypeId")
    private LoanType loanType;

    @Column(nullable = false)
    private Double requestedAmount;

    @Column(nullable = false)
    private Integer loanTerm;
    
    @Column(nullable = false)
    private LocalDate requestDate = LocalDate.now();

    @Column(nullable = false)
    private String status = "Pending";

    @ManyToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "managerId")
    private Manager manager;

    @Column
    private Boolean isEligible = false;


    @Column
    private LocalDate approvalDate;
    
    @Column
    private String documentPath; 
}
