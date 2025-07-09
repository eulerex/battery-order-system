package com.battery.ordersystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @GetMapping("/")
    public String hello() {
        return "电池订单系统运行正常！";
    }
    
    @GetMapping("/fix-orders-table")
    public String fixOrdersTable() {
        try {
            // 检查是否存在customer_name字段
            String checkColumn = "SELECT column_name FROM information_schema.columns " +
                               "WHERE table_name = 'orders' AND column_name = 'customer_name'";
            
            var result = jdbcTemplate.queryForList(checkColumn);
            
            if (!result.isEmpty()) {
                // 如果存在customer_name字段，重命名为name
                jdbcTemplate.execute("ALTER TABLE orders RENAME COLUMN customer_name TO name");
                return "成功将orders表的customer_name字段重命名为name";
            } else {
                // 检查是否已经存在name字段
                String checkNameColumn = "SELECT column_name FROM information_schema.columns " +
                                       "WHERE table_name = 'orders' AND column_name = 'name'";
                var nameResult = jdbcTemplate.queryForList(checkNameColumn);
                
                if (nameResult.isEmpty()) {
                    // 如果不存在name字段，添加它
                    jdbcTemplate.execute("ALTER TABLE orders ADD COLUMN name VARCHAR(100)");
                    return "成功为orders表添加name字段";
                } else {
                    return "orders表已经存在name字段，无需修改";
                }
            }
        } catch (Exception e) {
            return "修复orders表时出错: " + e.getMessage();
        }
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