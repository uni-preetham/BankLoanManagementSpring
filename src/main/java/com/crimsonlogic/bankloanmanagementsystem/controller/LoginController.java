package com.crimsonlogic.bankloanmanagementsystem.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crimsonlogic.bankloanmanagementsystem.dto.LoginRequestDTO;
import com.crimsonlogic.bankloanmanagementsystem.entity.Login;
import com.crimsonlogic.bankloanmanagementsystem.entity.User;
import com.crimsonlogic.bankloanmanagementsystem.entity.Manager;
import com.crimsonlogic.bankloanmanagementsystem.entity.Bank;
import com.crimsonlogic.bankloanmanagementsystem.service.LoginService;
import com.crimsonlogic.bankloanmanagementsystem.service.UserService;
import com.crimsonlogic.bankloanmanagementsystem.service.ManagerService;
import com.crimsonlogic.bankloanmanagementsystem.service.BankService;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "http://localhost:3000/", allowCredentials = "true") // adjust the React frontend URL
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService; // To fetch user details

    @Autowired
    private ManagerService managerService; // To fetch manager details

    @Autowired
    private BankService bankService; // To fetch bank details

    @PostMapping("/loginuser")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequestDTO loginRequest, HttpSession session) {
        Login login = loginService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        if (login != null) {
            // Check the role and store the corresponding entity in the session
            String roleName = login.getRole().getRoleName();
            switch (roleName) {
                case "USER":
                    User user = userService.findByLoginId(login.getLoginId());
                    session.setAttribute("user", user);  // Store user in session
                    return ResponseEntity.ok("/user/dashboard");

                case "MANAGER":
                    Manager manager = managerService.findByLoginId(login.getLoginId());
                    session.setAttribute("manager", manager);  // Store manager in session
                    return ResponseEntity.ok("/manager/dashboard");

                case "BANK":
                    Bank bank = bankService.findByLoginId(login.getLoginId());
                    session.setAttribute("bank", bank);  // Store bank in session
                    System.out.println(bank);
                    return ResponseEntity.ok("/bank/dashboard");

                default:
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
