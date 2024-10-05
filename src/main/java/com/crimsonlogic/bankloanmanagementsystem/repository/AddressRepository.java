package com.crimsonlogic.bankloanmanagementsystem.repository;

import com.crimsonlogic.bankloanmanagementsystem.entity.Address;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, String> {
	
	Address findByUserUserId(String userId);
}
