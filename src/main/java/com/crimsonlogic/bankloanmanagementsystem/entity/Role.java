package com.crimsonlogic.bankloanmanagementsystem.entity;

import com.crimsonlogic.bankloanmanagementsystem.util.IdGenerator;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "roles")
public class Role {
    
    @Id
    private String roleId;
    
    @PrePersist
    public void generateId() {
        this.roleId = IdGenerator.generateId("ROLE");
    }
    
    @Column(nullable = false, unique = true)
    private String roleName;
}
