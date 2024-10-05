package com.crimsonlogic.bankloanmanagementsystem.repository;

import com.crimsonlogic.bankloanmanagementsystem.entity.Occupation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OccupationRepository extends JpaRepository<Occupation, String> {

	Occupation findByUserUserId(String userId);
}
