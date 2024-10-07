package com.crimsonlogic.bankloanmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import com.crimsonlogic.bankloanmanagementsystem.util.IdGenerator;

@Data
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account {

	public Account(User user, Bank bank, LoanType loanType, Double approvedAmount, LocalDate approvalDate,
			Double interestRate, Integer durationYears) {
		super();
		this.user = user;
		this.bank = bank;
		this.loanType = loanType;
		this.approvedAmount = approvedAmount;
		this.approvalDate = approvalDate;
		this.interestRate = interestRate;
		this.durationYears = durationYears;
	}

	@Id
	private String accountId;

	@PrePersist
	public void generateId() {
		this.accountId = IdGenerator.generateId("ACC");
	}

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "bank_id", nullable = false)
	private Bank bank;

	@ManyToOne
	@JoinColumn(name = "loan_type_id", nullable = false)
	private LoanType loanType;

	private Double approvedAmount;

	private LocalDate approvalDate;

	private Double interestRate;

	private Integer durationYears;

	private Double emiAmount; // Store EMI amount
	private Double totalPayment; // Store total payment over the loan tenure

	@ElementCollection
	private List<PaymentDetail> paymentSchedule; // Store payment schedule details
}