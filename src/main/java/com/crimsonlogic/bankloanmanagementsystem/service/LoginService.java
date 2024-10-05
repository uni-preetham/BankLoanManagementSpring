package com.crimsonlogic.bankloanmanagementsystem.service;

import com.crimsonlogic.bankloanmanagementsystem.entity.Login;

public interface LoginService {

	Login authenticate(String email, String password);

}
