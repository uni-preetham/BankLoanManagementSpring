package com.crimsonlogic.bankloanmanagementsystem.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crimsonlogic.bankloanmanagementsystem.entity.Login;
import com.crimsonlogic.bankloanmanagementsystem.repository.LoginRepository;
import com.crimsonlogic.bankloanmanagementsystem.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService{

	@Autowired
    private LoginRepository loginRepository;

	@Override
    public Login authenticate(String email, String password) {
        Login login = loginRepository.findByEmail(email).get();
        if (login != null && login.getPassword().equals(password)) {
            return login; // Return login object if credentials match
        }
        return null; // Return null if credentials are invalid
    }
}
