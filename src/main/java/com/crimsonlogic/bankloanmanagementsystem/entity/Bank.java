package com.crimsonlogic.bankloanmanagementsystem.entity;

import java.util.List;

import com.crimsonlogic.bankloanmanagementsystem.util.IdGenerator;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "banks")
public class Bank {

    @Id
    private String bankId;
    
    @PrePersist
    public void generateId() {
        this.bankId = IdGenerator.generateId("BAN");
    }

    @ManyToOne
    @JoinColumn(name = "login_id", referencedColumnName = "loginId")
    private Login login;

    @Column(nullable = false, unique = true)
    private String bankName;

    @Column(length=200)
    private String branchAddress;

    @Column(length=10)
    private String contactNumber;
    
    @OneToMany(mappedBy = "bank", fetch = FetchType.EAGER)
    private List<LoanType> loanTypes; // Add this field to fetch loan types

    @Override
    public String toString() {
        return "Bank{" +
                "bankId='" + bankId + '\'' +
                ", bankName='" + bankName + '\'' +
                ", branchAddress='" + branchAddress + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                '}';
    }

}
