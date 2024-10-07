package com.crimsonlogic.bankloanmanagementsystem.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crimsonlogic.bankloanmanagementsystem.dto.ManagerDTO;
import com.crimsonlogic.bankloanmanagementsystem.dto.ManagerRegistrationDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.Bank;
import com.crimsonlogic.bankloanmanagementsystem.entity.Login;
import com.crimsonlogic.bankloanmanagementsystem.entity.Manager;
import com.crimsonlogic.bankloanmanagementsystem.entity.Role;
import com.crimsonlogic.bankloanmanagementsystem.repository.LoginRepository;
import com.crimsonlogic.bankloanmanagementsystem.repository.ManagerRepository;
import com.crimsonlogic.bankloanmanagementsystem.repository.RoleRepository;
import com.crimsonlogic.bankloanmanagementsystem.service.ManagerService;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public void registerManager(ManagerRegistrationDTO managerDTO, Bank bank) {
        // Create and save the Login entity
        Login login = createLogin(managerDTO);
        loginRepository.save(login);

        // Create and save the Manager entity with bank information
        Manager manager = createManager(managerDTO, login, bank);
        managerRepository.save(manager);
    }

    private Login createLogin(ManagerRegistrationDTO managerDTO) {
        Login login = new Login();
        login.setEmail(managerDTO.getEmail());
        login.setPassword(managerDTO.getPassword()); // Ensure password encoding
        Role managerRole = roleRepository.findByRoleName("MANAGER");
        login.setRole(managerRole);
        return login;
    }

    private Manager createManager(ManagerRegistrationDTO managerDTO, Login login, Bank bank) {
        Manager manager = new Manager();
        manager.setFirstName(managerDTO.getFirstName());
        manager.setLastName(managerDTO.getLastName());
        manager.setEmail(managerDTO.getEmail());
        manager.setPhone(managerDTO.getPhone());
        manager.setLogin(login);
        manager.setBank(bank); // Set bank for manager
        return manager;
    }
    
    @Override
    public Manager findByLoginId(String loginId) {
        return managerRepository.findByLogin_LoginId(loginId);
    }
    
    @Override
    public Manager updateManagerProfile(String managerId, ManagerDTO managerDTO) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found with id: " + managerId));

        // Update manager fields with the new data
        manager.setFirstName(managerDTO.getFirstName());
        manager.setLastName(managerDTO.getLastName());
        manager.setEmail(managerDTO.getEmail());
        manager.setPhone(managerDTO.getPhone());

        // Save the updated manager
        managerRepository.save(manager);
        return manager;
    }
    
    @Override
    public List<Manager> getAllManagers(){
    	return managerRepository.findAll();
    }

    @Override
    public void deleteManagerById(String managerId) {
    	managerRepository.deleteById(managerId);
    }

}
