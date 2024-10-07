package com.crimsonlogic.bankloanmanagementsystem.entity;

import com.crimsonlogic.bankloanmanagementsystem.util.IdGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "loan_types")
public class LoanType {

    @Id
    private String loanTypeId;
    
    @PrePersist
    public void generateId() {
        this.loanTypeId = IdGenerator.generateId("LT");
    }

    @ManyToOne
    @JoinColumn(name = "bank_id", referencedColumnName = "bankId")
    @JsonIgnore
    private Bank bank;

    @Column(nullable = false)
    private String loanName;

    @Column(nullable = false)
    private Double interestRate;

    @Column(nullable = false)
    private Double maxAmount;

    @Column(nullable = false)
    private Integer durationYears;
   
}
