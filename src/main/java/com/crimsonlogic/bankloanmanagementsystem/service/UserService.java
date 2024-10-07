package com.crimsonlogic.bankloanmanagementsystem.service;


import com.crimsonlogic.bankloanmanagementsystem.dto.UserRegistrationDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.User;

public interface UserService {

	void registerUser(UserRegistrationDTO registrationDTO);

	User findByLoginId(String loginId);

	UserRegistrationDTO getUserProfile(String userId);
    User updateUserProfile(String userId, UserRegistrationDTO userDTO);

	User getUser(String userId);

	void saveUser(User user);

	void changePassword(String userId, String currentPassword, String newPassword);

}
