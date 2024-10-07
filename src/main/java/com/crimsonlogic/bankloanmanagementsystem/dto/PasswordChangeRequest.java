package com.crimsonlogic.bankloanmanagementsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordChangeRequest {
	 private String userId;
    private String currentPassword;
    private String newPassword;

}
