package com.crimsonlogic.bankloanmanagementsystem.dto;

import lombok.Data;

@Data
public class UserRegistrationDTO {
	private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Double salary;
    private Integer creditScore;
}
