package com.crimsonlogic.bankloanmanagementsystem.service;

import java.util.List;

import com.crimsonlogic.bankloanmanagementsystem.dto.ManagerDTO;
import com.crimsonlogic.bankloanmanagementsystem.dto.ManagerRegistrationDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.Bank;
import com.crimsonlogic.bankloanmanagementsystem.entity.Manager;

public interface ManagerService {


	Manager findByLoginId(String loginId);

	void registerManager(ManagerRegistrationDTO managerDTO, Bank bank);

	Manager updateManagerProfile(String managerId, ManagerDTO managerDTO);

	List<Manager> getAllManagers();

	void deleteManagerById(String managerId);
	
}
