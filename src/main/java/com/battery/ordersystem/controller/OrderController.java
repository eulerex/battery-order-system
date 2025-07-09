package com.battery.ordersystem.controller;

import com.battery.ordersystem.dto.OrderRequest;
import com.battery.ordersystem.dto.OrderResponse;
import com.battery.ordersystem.entity.Order;
import com.battery.ordersystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    // 创建订单
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
        try {
            OrderResponse order = orderService.createOrder(request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "订单创建成功");
            response.put("data", order);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "订单创建失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 获取订单列表（分页）
    @GetMapping
    public ResponseEntity<?> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<OrderResponse> orders = orderService.getOrders(page, size);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", orders.getContent());
            response.put("totalElements", orders.getTotalElements());
            response.put("totalPages", orders.getTotalPages());
            response.put("currentPage", orders.getNumber());
            response.put("size", orders.getSize());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取订单列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 根据ID获取订单
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
        try {
            Optional<OrderResponse> order = orderService.getOrderById(orderId);
            if (order.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", order.get());
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "订单不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取订单失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 根据订单号获取订单
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<?> getOrderByNumber(@PathVariable String orderNumber) {
        try {
            Optional<OrderResponse> order = orderService.getOrderByNumber(orderNumber);
            if (order.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", order.get());
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "订单不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取订单失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 更新订单状态
    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {
        try {
            Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            Optional<OrderResponse> order = orderService.updateOrderStatus(orderId, orderStatus);
            if (order.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "订单状态更新成功");
                response.put("data", order.get());
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "订单不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "无效的订单状态");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "更新订单状态失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 删除订单
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        try {
            boolean deleted = orderService.deleteOrder(orderId);
            if (deleted) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "订单删除成功");
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "订单不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "删除订单失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 搜索订单（按客户姓名）
    @GetMapping("/search/customer")
    public ResponseEntity<?> searchOrdersByCustomerName(
            @RequestParam String customerName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<OrderResponse> orders = orderService.searchOrdersByCustomerName(customerName, page, size);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", orders.getContent());
            response.put("totalElements", orders.getTotalElements());
            response.put("totalPages", orders.getTotalPages());
            response.put("currentPage", orders.getNumber());
            response.put("size", orders.getSize());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "搜索订单失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 搜索订单（按产品名称）
    @GetMapping("/search/product")
    public ResponseEntity<?> searchOrdersByProductName(
            @RequestParam String productName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<OrderResponse> orders = orderService.searchOrdersByProductName(productName, page, size);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", orders.getContent());
            response.put("totalElements", orders.getTotalElements());
            response.put("totalPages", orders.getTotalPages());
            response.put("currentPage", orders.getNumber());
            response.put("size", orders.getSize());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "搜索订单失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 根据状态筛选订单
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getOrdersByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            Page<OrderResponse> orders = orderService.getOrdersByStatus(orderStatus, page, size);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", orders.getContent());
            response.put("totalElements", orders.getTotalElements());
            response.put("totalPages", orders.getTotalPages());
            response.put("currentPage", orders.getNumber());
            response.put("size", orders.getSize());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "无效的订单状态");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取订单失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 获取最近的订单
    @GetMapping("/recent")
    public ResponseEntity<?> getRecentOrders() {
        try {
            List<OrderResponse> orders = orderService.getRecentOrders();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", orders);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取最近订单失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // 获取订单统计信息
    @GetMapping("/statistics")
    public ResponseEntity<?> getOrderStatistics() {
        try {
            OrderService.OrderStatistics stats = orderService.getStatistics();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", stats);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取统计信息失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 