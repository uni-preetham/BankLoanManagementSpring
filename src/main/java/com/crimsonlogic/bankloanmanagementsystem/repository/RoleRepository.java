package com.crimsonlogic.bankloanmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimsonlogic.bankloanmanagementsystem.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String>{
	public Role findByRoleName(String roleName);
}
