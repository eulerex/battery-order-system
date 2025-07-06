package com.battery.ordersystem.service;

import com.battery.ordersystem.dto.LoginRequest;
import com.battery.ordersystem.dto.LoginResponse;
import com.battery.ordersystem.entity.User;
import com.battery.ordersystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public LoginResponse login(LoginRequest request) {
        LoginResponse response = new LoginResponse();
        
        // 查找用户
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        
        if (userOpt.isEmpty()) {
            response.setSuccess(false);
            response.setMessage("用户不存在");
            return response;
        }
        
        User user = userOpt.get();
        
        // 检查账户是否被锁定
        if (user.getAccountLocked()) {
            response.setSuccess(false);
            response.setMessage("账户已被锁定，请联系管理员");
            return response;
        }
        
        // 检查密码（这里简化处理，实际应该加密比较）
        if (!user.getPassword().equals(request.getPassword())) {
            // 增加登录失败次数
            user.setLoginAttempts(user.getLoginAttempts() + 1);
            
            // 如果失败次数超过5次，锁定账户
            if (user.getLoginAttempts() >= 5) {
                user.setAccountLocked(true);
            }
            
            userRepository.save(user);
            
            response.setSuccess(false);
            response.setMessage("密码错误，失败次数: " + user.getLoginAttempts());
            return response;
        }
        
        // 登录成功，重置失败次数
        user.setLoginAttempts(0);
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
        
        // 生成简单的token（实际应该使用JWT）
        String token = "token_" + user.getUserId() + "_" + System.currentTimeMillis();
        
        response.setSuccess(true);
        response.setMessage("登录成功");
        response.setToken(token);
        response.setRole(user.getRole());
        response.setEmail(user.getEmail());
        
        return response;
    }
    
    public User register(String email, String password, String role) {
        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("邮箱已存在");
        }
        
        User user = new User();
        user.setEmail(email);
        user.setPassword(password); // 实际应该加密
        user.setRole(role);
        
        return userRepository.save(user);
    }
} 