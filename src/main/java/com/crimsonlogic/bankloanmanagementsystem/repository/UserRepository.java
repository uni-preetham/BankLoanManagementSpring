package com.crimsonlogic.bankloanmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.bankloanmanagementsystem.entity.User;

public interface UserRepository extends JpaRepository<User, String>{
	User findByLogin_LoginId(String loginId); // Find user by loginId
}
