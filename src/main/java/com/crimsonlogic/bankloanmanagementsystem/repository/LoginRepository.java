package com.crimsonlogic.bankloanmanagementsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.bankloanmanagementsystem.entity.Login;

public interface LoginRepository extends JpaRepository<Login, String>{
	Optional<Login> findByEmail(String email); // Find a user by email

}
