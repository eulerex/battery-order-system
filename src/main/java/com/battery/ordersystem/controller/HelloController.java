package com.battery.ordersystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    @GetMapping("/")
    public String home() {
        return """
            <!DOCTYPE html>
            <html lang="zh-CN">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>电池订单系统</title>
                <style>
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        margin: 0;
                        padding: 0;
                        min-height: 100vh;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                    }
                    .container {
                        background: white;
                        padding: 40px;
                        border-radius: 10px;
                        box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
                        text-align: center;
                        max-width: 500px;
                    }
                    h1 {
                        color: #333;
                        margin-bottom: 20px;
                    }
                    p {
                        color: #666;
                        margin-bottom: 30px;
                    }
                    .btn {
                        display: inline-block;
                        padding: 12px 24px;
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        color: white;
                        text-decoration: none;
                        border-radius: 5px;
                        font-weight: 500;
                        transition: transform 0.2s ease;
                    }
                    .btn:hover {
                        transform: translateY(-2px);
                    }
                    .links {
                        margin-top: 30px;
                    }
                    .links a {
                        display: block;
                        margin: 10px 0;
                        color: #667eea;
                        text-decoration: none;
                    }
                    .links a:hover {
                        text-decoration: underline;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>欢迎使用电池订单系统！</h1>
                    <p>这是一个完整的订单管理系统，包含用户认证、订单管理等功能。</p>
                    <a href="/login.html" class="btn">立即登录</a>
                    <div class="links">
                        <a href="/ping">服务健康检查</a>
                        <a href="/health">系统状态</a>
                        <a href="/api/auth/test">认证服务测试</a>
                    </div>
                </div>
            </body>
            </html>
            """;
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