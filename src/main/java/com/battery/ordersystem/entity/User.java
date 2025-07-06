package com.battery.ordersystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "role")
    private String role;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "login_attempts")
    private Integer loginAttempts = 0;
    
    @Column(name = "account_locked")
    private Boolean accountLocked = false;
    
    @Column(name = "password_expires_at")
    private LocalDateTime passwordExpiresAt;
    
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 