package com.crimsonlogic.bankloanmanagementsystem.controller;

import com.crimsonlogic.bankloanmanagementsystem.entity.Occupation;
import com.crimsonlogic.bankloanmanagementsystem.entity.User;
import com.crimsonlogic.bankloanmanagementsystem.service.OccupationService;
import com.crimsonlogic.bankloanmanagementsystem.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000/", allowCredentials = "true") // adjust the React frontend URL
@RestController
@RequestMapping("/api/occupation")
public class OccupationController {

    @Autowired
    private OccupationService occupationService;
    @Autowired
    private UserService userService;

    // Endpoint to get occupation details by user ID
    @GetMapping("/{userId}")
    public ResponseEntity<Occupation> getOccupationByUserId(@PathVariable String userId) {
        Occupation occupation = occupationService.getOccupationByUserId(userId);
        return ResponseEntity.ok(occupation);
    }

    // Endpoint to add or update occupation
    @PostMapping("/add/{userId}")
    public ResponseEntity<Occupation> addOrUpdateOccupation(@PathVariable String userId, @RequestBody Occupation occupation, HttpSession session) {
        Occupation updatedOccupation = occupationService.saveOccupation(userId, occupation);
        User user = userService.getUser(userId);
        session.setAttribute("user", user);
        System.out.println(user);
        return ResponseEntity.ok(updatedOccupation);
    }
}
