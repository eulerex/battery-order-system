package com.battery.ordersystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    @GetMapping("/")
    public String home() {
        return "欢迎使用电池订单系统！";
    }
    
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
    
    @GetMapping("/health")
    public String health() {
        return "服务运行正常";
    }
} 