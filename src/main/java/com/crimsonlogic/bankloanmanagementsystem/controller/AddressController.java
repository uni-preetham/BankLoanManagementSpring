package com.crimsonlogic.bankloanmanagementsystem.controller;

import com.crimsonlogic.bankloanmanagementsystem.entity.Address;
import com.crimsonlogic.bankloanmanagementsystem.entity.User;
import com.crimsonlogic.bankloanmanagementsystem.service.AddressService;
import com.crimsonlogic.bankloanmanagementsystem.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/*
 * @author Preetham
*/
@CrossOrigin(origins = "http://localhost:3000/", allowCredentials = "true") // adjust the React frontend URL
@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private AddressService addressService;
    
    @Autowired
    private UserService userService;

    // Endpoint to get address details by user ID
    @GetMapping("/{userId}")
    public ResponseEntity<Address> getAddressByUserId(@PathVariable String userId) {
        Address address = addressService.getAddressByUserId(userId);
        return ResponseEntity.ok(address);
    }

    // Endpoint to add or update address
    @PostMapping("/add/{userId}")
    public ResponseEntity<Address> addOrUpdateAddress(@PathVariable String userId, @RequestBody Address address, HttpSession session) {
        Address updatedAddress = addressService.saveAddress(userId, address);
        User user = userService.getUser(userId);
        session.setAttribute("user", user);
        System.out.println(user);
        return ResponseEntity.ok(updatedAddress);
    }
}
