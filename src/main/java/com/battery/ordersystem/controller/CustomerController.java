package com.battery.ordersystem.controller;

import com.battery.ordersystem.dto.CustomerRequest;
import com.battery.ordersystem.dto.CustomerResponse;
import com.battery.ordersystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    // 创建客户
    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody CustomerRequest request) {
        try {
            CustomerResponse customer = customerService.createCustomer(request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "客户创建成功");
            response.put("data", customer);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "客户创建失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 获取客户列表（分页）
    @GetMapping
    public ResponseEntity<?> getCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<CustomerResponse> customers = customerService.getCustomers(page, size);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", customers.getContent());
            response.put("totalElements", customers.getTotalElements());
            response.put("totalPages", customers.getTotalPages());
            response.put("currentPage", customers.getNumber());
            response.put("size", customers.getSize());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取客户列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 根据ID获取客户
    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long customerId) {
        try {
            Optional<CustomerResponse> customer = customerService.getCustomerById(customerId);
            if (customer.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", customer.get());
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "客户不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取客户失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 更新客户信息
    @PutMapping("/{customerId}")
    public ResponseEntity<?> updateCustomer(
            @PathVariable Long customerId,
            @RequestBody CustomerRequest request) {
        try {
            Optional<CustomerResponse> customer = customerService.updateCustomer(customerId, request);
            if (customer.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "客户信息更新成功");
                response.put("data", customer.get());
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "客户不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "更新客户信息失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 删除客户
    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long customerId) {
        try {
            boolean deleted = customerService.deleteCustomer(customerId);
            if (deleted) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "客户删除成功");
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "客户不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "删除客户失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 搜索客户
    @GetMapping("/search")
    public ResponseEntity<?> searchCustomers(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<CustomerResponse> customers = customerService.searchCustomers(keyword, page, size);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", customers.getContent());
            response.put("totalElements", customers.getTotalElements());
            response.put("totalPages", customers.getTotalPages());
            response.put("currentPage", customers.getNumber());
            response.put("size", customers.getSize());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "搜索客户失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 根据客户类型筛选
    @GetMapping("/type/{customerType}")
    public ResponseEntity<?> getCustomersByType(
            @PathVariable String customerType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<CustomerResponse> customers = customerService.getCustomersByType(customerType, page, size);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", customers.getContent());
            response.put("totalElements", customers.getTotalElements());
            response.put("totalPages", customers.getTotalPages());
            response.put("currentPage", customers.getNumber());
            response.put("size", customers.getSize());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取客户列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 根据状态筛选
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getCustomersByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<CustomerResponse> customers = customerService.getCustomersByStatus(status, page, size);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", customers.getContent());
            response.put("totalElements", customers.getTotalElements());
            response.put("totalPages", customers.getTotalPages());
            response.put("currentPage", customers.getNumber());
            response.put("size", customers.getSize());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取客户列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 获取最近客户
    @GetMapping("/recent")
    public ResponseEntity<?> getRecentCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            Page<CustomerResponse> customers = customerService.getRecentCustomers(page, size);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", customers.getContent());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取最近客户失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 获取客户统计信息
    @GetMapping("/statistics")
    public ResponseEntity<?> getCustomerStatistics() {
        try {
            Map<String, Object> stats = customerService.getCustomerStatistics();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", stats);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取客户统计失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 更新客户状态
    @PutMapping("/{customerId}/status")
    public ResponseEntity<?> updateCustomerStatus(
            @PathVariable Long customerId,
            @RequestParam String status) {
        try {
            Optional<CustomerResponse> customer = customerService.updateCustomerStatus(customerId, status);
            if (customer.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "客户状态更新成功");
                response.put("data", customer.get());
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "客户不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "更新客户状态失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 