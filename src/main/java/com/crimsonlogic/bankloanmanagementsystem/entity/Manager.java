package com.crimsonlogic.bankloanmanagementsystem.entity;

import com.crimsonlogic.bankloanmanagementsystem.util.IdGenerator;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "managers")
public class Manager {

    @Id
    private String managerId;
    
    @PrePersist
    public void generateId() {
        this.managerId = IdGenerator.generateId("MAN");
    }

    @OneToOne
    @JoinColumn(name = "login_id", referencedColumnName = "loginId")
    private Login login;

    @ManyToOne
    @JoinColumn(name = "bank_id", referencedColumnName = "bankId")
    private Bank bank;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String phone;
}
