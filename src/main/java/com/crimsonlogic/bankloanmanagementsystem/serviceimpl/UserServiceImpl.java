package com.crimsonlogic.bankloanmanagementsystem.serviceimpl;

import com.crimsonlogic.bankloanmanagementsystem.dto.UserRegistrationDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.Login;
import com.crimsonlogic.bankloanmanagementsystem.entity.Role;
import com.crimsonlogic.bankloanmanagementsystem.entity.User;
import com.crimsonlogic.bankloanmanagementsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.bankloanmanagementsystem.exception.UserAlreadyExistsException;
import com.crimsonlogic.bankloanmanagementsystem.repository.LoginRepository;
import com.crimsonlogic.bankloanmanagementsystem.repository.RoleRepository;
import com.crimsonlogic.bankloanmanagementsystem.repository.UserRepository;
import com.crimsonlogic.bankloanmanagementsystem.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Override
    public void saveUser(User user) {
    	userRepository.save(user);
    }

    @Override
    public void registerUser(UserRegistrationDTO registrationDTO) {
        // Find role by name (assuming roles are predefined)
        Role role = roleRepository.findByRoleName("USER");
        
        // Check if email already exists in login table
        if (loginRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists.");
        }
        
        // Create Login entity
        Login login = new Login();
        login.setEmail(registrationDTO.getEmail());
        login.setPassword(registrationDTO.getPassword());
        login.setRole(role);
        loginRepository.save(login);
        
        // Create User entity
        User user = new User();
        user.setLogin(login);
        user.setFirstName(registrationDTO.getFirstName());
        user.setLastName(registrationDTO.getLastName());
        user.setPhone(registrationDTO.getPhone());
        user.setCreditScore(800);
        userRepository.save(user);
    }
    
    
    @Override
    public User findByLoginId(String loginId) {
        return userRepository.findByLogin_LoginId(loginId);
    }
    
    @Override
    public UserRegistrationDTO getUserProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Convert User entity to DTO
        UserRegistrationDTO userDTO = new UserRegistrationDTO();
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPhone(user.getPhone());
        userDTO.setCreditScore(user.getCreditScore());

        return userDTO;
    }

    @Override
    public User updateUserProfile(String userId, UserRegistrationDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Update user fields with the new data
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhone(userDTO.getPhone());
        user.setCreditScore(userDTO.getCreditScore());

        // Save the updated user
        userRepository.save(user);
        return user;
    }
    
    
    @Override 
    public User getUser(String userId) {
    	return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with userid "+userId+" not found"));
    }
    
   
}
