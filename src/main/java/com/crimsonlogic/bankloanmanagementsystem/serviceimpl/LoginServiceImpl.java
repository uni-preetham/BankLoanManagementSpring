package com.crimsonlogic.bankloanmanagementsystem.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.crimsonlogic.bankloanmanagementsystem.entity.Login;
import com.crimsonlogic.bankloanmanagementsystem.exception.ResourceNotFoundException;
import com.crimsonlogic.bankloanmanagementsystem.repository.LoginRepository;
import com.crimsonlogic.bankloanmanagementsystem.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService{

	@Autowired
    private LoginRepository loginRepository;
	
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;

	@Override
    public Login authenticate(String email, String password) {
        Login login = loginRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("Email "+email+" not found"));
        if (login != null && passwordEncoder.matches(password, login.getPassword())) {
            return login; // Return login object if credentials match
        }
        return null; // Return null if credentials are invalid
    }
}
