package com.crimsonlogic.bankloanmanagementsystem.entity;

import com.crimsonlogic.bankloanmanagementsystem.util.IdGenerator;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "login")
public class Login {

    @Id
    private String loginId;
    
    @PrePersist
    public void generateId() {
        this.loginId = IdGenerator.generateId("LOG");
    }
    
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "roleId")
    private Role role;
}
