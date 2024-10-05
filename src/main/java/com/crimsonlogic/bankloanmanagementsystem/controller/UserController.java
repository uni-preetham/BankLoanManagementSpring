package com.crimsonlogic.bankloanmanagementsystem.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crimsonlogic.bankloanmanagementsystem.dto.UserRegistrationDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.User;
import com.crimsonlogic.bankloanmanagementsystem.service.UserService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000/", allowCredentials = "true")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/details")
	public ResponseEntity<User> getUserDetails(HttpSession session) {
		// Retrieve the user object from session
		User user = (User) session.getAttribute("user");
		

		if (user != null) {
			return ResponseEntity.ok(user); // Return the user details
		} else {
			return ResponseEntity.status(401).build(); // Unauthorized if no user in session
		}
	}
	

	// Get user details for editing
	@GetMapping("/edit/{userId}")
	public UserRegistrationDTO getUserProfile(@PathVariable("userId") String userId) {
		return userService.getUserProfile(userId);
	}

	// Update user details
	@PutMapping("/edit/{userId}")
	public ResponseEntity<User> updateUserProfile(@PathVariable("userId") String userId, @RequestBody UserRegistrationDTO userDTO, HttpSession session) {
	    User user = userService.updateUserProfile(userId, userDTO);
	    session.setAttribute("user", user);
	    return ResponseEntity.ok(user); // Return the updated user object
	}
	
	
	//to logout
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {
	    HttpSession session = request.getSession(false);		    // Invalidate the session
	    if (session != null) {
	        session.invalidate();
	    }
	    return ResponseEntity.ok("Logout successful");
	}
}
