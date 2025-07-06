package com.battery.ordersystem.controller;

import com.battery.ordersystem.dto.LoginRequest;
import com.battery.ordersystem.dto.LoginResponse;
import com.battery.ordersystem.entity.User;
import com.battery.ordersystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest request) {
        try {
            User user = userService.register(request.getEmail(), request.getPassword(), "user");
            return ResponseEntity.ok("注册成功，用户ID: " + user.getUserId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("注册失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("认证服务正常运行");
    }
} 