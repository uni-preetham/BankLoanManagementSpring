package com.crimsonlogic.bankloanmanagementsystem.entity;


import com.crimsonlogic.bankloanmanagementsystem.util.IdGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Occupation {
    @Id
    private String occupationId;
    
    @PrePersist
    public void generateId() {
        this.occupationId = IdGenerator.generateId("OCC");
    }
    
    @Column(length=100, nullable=false)
    private String occupationType;

    @Column(length = 100, nullable = false)
    private String occupationName;

    @Column(length = 100)
    private String companyName;
    
    @Column(length=10)
    private  Double salary;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

}
