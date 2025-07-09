package com.battery.ordersystem.repository;

import com.battery.ordersystem.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // 根据订单号查找订单
    Optional<Order> findByOrderNumber(String orderNumber);
    
    // 根据客户姓名查找订单
    List<Order> findByNameContainingIgnoreCase(String name);
    
    // 根据产品名称查找订单
    List<Order> findByProductNameContainingIgnoreCase(String productName);
    
    // 根据状态查找订单
    List<Order> findByStatus(Order.OrderStatus status);
    
    // 根据订单日期范围查找订单
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // 分页查询所有订单，按创建时间倒序
    Page<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    // 根据状态分页查询订单
    Page<Order> findByStatusOrderByCreatedAtDesc(Order.OrderStatus status, Pageable pageable);
    
    // 根据客户姓名分页查询订单
    Page<Order> findByNameContainingIgnoreCaseOrderByCreatedAtDesc(String name, Pageable pageable);
    
    // 根据产品名称分页查询订单
    Page<Order> findByProductNameContainingIgnoreCaseOrderByCreatedAtDesc(String productName, Pageable pageable);
    
    // 复合查询：根据状态和客户姓名
    Page<Order> findByStatusAndNameContainingIgnoreCaseOrderByCreatedAtDesc(
            Order.OrderStatus status, String name, Pageable pageable);
    
    // 统计今日订单数量
    @Query("SELECT COUNT(o) FROM Order o WHERE o.createdAt >= CURRENT_DATE")
    long countTodayOrders();
    
    // 统计本月订单数量
    @Query("SELECT COUNT(o) FROM Order o WHERE YEAR(o.createdAt) = YEAR(CURRENT_DATE) AND MONTH(o.createdAt) = MONTH(CURRENT_DATE)")
    long countThisMonthOrders();
    
    // 统计待处理订单数量
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'PENDING'")
    long countPendingOrders();
    
    // 统计本月收入
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE EXTRACT(YEAR FROM o.createdAt) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM o.createdAt) = EXTRACT(MONTH FROM CURRENT_DATE) AND o.status = 'COMPLETED'")
    double getMonthlyRevenue();
    
    // 根据客户姓名统计订单数量
    @Query("SELECT COUNT(o) FROM Order o WHERE o.name = :name")
    long countByName(@Param("name") String name);
    
    // 根据客户姓名统计订单总金额
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.name = :name")
    Double sumTotalAmountByName(@Param("name") String name);
    
    // 获取最近的订单
    List<Order> findTop10ByOrderByCreatedAtDesc();
} 