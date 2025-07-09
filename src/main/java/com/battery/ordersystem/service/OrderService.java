package com.battery.ordersystem.service;

import com.battery.ordersystem.dto.OrderRequest;
import com.battery.ordersystem.dto.OrderResponse;
import com.battery.ordersystem.entity.Order;
import com.battery.ordersystem.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    // 生成订单号
    private String generateOrderNumber() {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "ORD-" + timestamp;
    }
    
    // 创建订单
    public OrderResponse createOrder(OrderRequest request) {
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setName(request.getName());
        order.setCustomerPhone(request.getCustomerPhone());
        order.setCustomerEmail(request.getCustomerEmail());
        order.setProductName(request.getProductName());
        order.setProductSpecification(request.getProductSpecification());
        order.setQuantity(request.getQuantity());
        order.setUnitPrice(request.getUnitPrice());
        order.setTotalAmount(request.getUnitPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
        order.setOrderDate(LocalDateTime.now());
        order.setDeliveryDate(request.getDeliveryDate());
        order.setNotes(request.getNotes());
        order.setStatus(Order.OrderStatus.PENDING);
        
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.fromOrder(savedOrder);
    }
    
    // 获取订单列表（分页）
    public Page<OrderResponse> getOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findAllByOrderByCreatedAtDesc(pageable);
        return orders.map(OrderResponse::fromOrder);
    }
    
    // 根据ID获取订单
    public Optional<OrderResponse> getOrderById(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        return order.map(OrderResponse::fromOrder);
    }
    
    // 根据订单号获取订单
    public Optional<OrderResponse> getOrderByNumber(String orderNumber) {
        Optional<Order> order = orderRepository.findByOrderNumber(orderNumber);
        return order.map(OrderResponse::fromOrder);
    }
    
    // 更新订单状态
    public Optional<OrderResponse> updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(status);
            order.setUpdatedAt(LocalDateTime.now());
            Order savedOrder = orderRepository.save(order);
            return Optional.of(OrderResponse.fromOrder(savedOrder));
        }
        return Optional.empty();
    }
    
    // 删除订单
    public boolean deleteOrder(Long orderId) {
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
            return true;
        }
        return false;
    }
    
    // 根据客户姓名搜索订单
    public Page<OrderResponse> searchOrdersByCustomerName(String customerName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findByNameContainingIgnoreCaseOrderByCreatedAtDesc(customerName, pageable);
        return orders.map(OrderResponse::fromOrder);
    }
    
    // 根据产品名称搜索订单
    public Page<OrderResponse> searchOrdersByProductName(String productName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findByProductNameContainingIgnoreCaseOrderByCreatedAtDesc(productName, pageable);
        return orders.map(OrderResponse::fromOrder);
    }
    
    // 根据状态筛选订单
    public Page<OrderResponse> getOrdersByStatus(Order.OrderStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
        return orders.map(OrderResponse::fromOrder);
    }
    
    // 获取最近的订单
    public List<OrderResponse> getRecentOrders() {
        List<Order> orders = orderRepository.findTop10ByOrderByCreatedAtDesc();
        return orders.stream()
                .map(OrderResponse::fromOrder)
                .collect(Collectors.toList());
    }
    
    // 获取统计信息
    public OrderStatistics getStatistics() {
        OrderStatistics stats = new OrderStatistics();
        stats.setTodayOrders(orderRepository.countTodayOrders());
        stats.setThisMonthOrders(orderRepository.countThisMonthOrders());
        stats.setPendingOrders(orderRepository.countPendingOrders());
        stats.setMonthlyRevenue(BigDecimal.valueOf(orderRepository.getMonthlyRevenue()));
        return stats;
    }
    
    // 统计信息内部类
    public static class OrderStatistics {
        private long todayOrders;
        private long thisMonthOrders;
        private long pendingOrders;
        private BigDecimal monthlyRevenue;
        
        // Getters and Setters
        public long getTodayOrders() {
            return todayOrders;
        }
        
        public void setTodayOrders(long todayOrders) {
            this.todayOrders = todayOrders;
        }
        
        public long getThisMonthOrders() {
            return thisMonthOrders;
        }
        
        public void setThisMonthOrders(long thisMonthOrders) {
            this.thisMonthOrders = thisMonthOrders;
        }
        
        public long getPendingOrders() {
            return pendingOrders;
        }
        
        public void setPendingOrders(long pendingOrders) {
            this.pendingOrders = pendingOrders;
        }
        
        public BigDecimal getMonthlyRevenue() {
            return monthlyRevenue;
        }
        
        public void setMonthlyRevenue(BigDecimal monthlyRevenue) {
            this.monthlyRevenue = monthlyRevenue;
        }
    }
} 