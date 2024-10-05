package com.crimsonlogic.bankloanmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.crimsonlogic.bankloanmanagementsystem.entity.LoanType;

public interface LoanTypeRepository extends JpaRepository<LoanType, String> {
}
