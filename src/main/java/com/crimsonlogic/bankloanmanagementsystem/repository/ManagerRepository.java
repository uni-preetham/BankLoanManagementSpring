package com.crimsonlogic.bankloanmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.bankloanmanagementsystem.entity.Manager;

public interface ManagerRepository extends JpaRepository<Manager, String>{
	Manager findByLogin_LoginId(String loginId); // Find user by loginId
}
