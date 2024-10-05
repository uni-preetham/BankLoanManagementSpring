package com.crimsonlogic.bankloanmanagementsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ManagerRegistrationDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
//    private String bankId; 
}
