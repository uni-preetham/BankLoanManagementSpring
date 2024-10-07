package com.crimsonlogic.bankloanmanagementsystem.entity;

import com.crimsonlogic.bankloanmanagementsystem.util.IdGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_details")
@Data
@NoArgsConstructor
public class PaymentDetail {
	@Id
    private String paymentId;
    
    @PrePersist
    public void generateId() {
        this.paymentId = IdGenerator.generateId("PAY");
    }
    private Integer month;
    private Double emi;
    private Double principalPayment;
    private Double interestPayment;
    private Double outstandingPrincipal;

    public PaymentDetail(Integer month, Double emi, Double principalPayment, Double interestPayment, Double outstandingPrincipal) {
        this.month = month;
        this.emi = emi;
        this.principalPayment = principalPayment;
        this.interestPayment = interestPayment;
        this.outstandingPrincipal = outstandingPrincipal;
    }
}