package com.battery.ordersystem.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private boolean success;
    private String message;
    private String token;
    private String role;
    private String email;
} 