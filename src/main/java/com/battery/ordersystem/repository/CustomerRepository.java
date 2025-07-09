package com.battery.ordersystem.repository;

import com.battery.ordersystem.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // 根据客户姓名查找
    Optional<Customer> findByName(String name);
    
    // 根据邮箱查找
    Optional<Customer> findByEmail(String email);
    
    // 根据电话查找
    Optional<Customer> findByPhone(String phone);
    
    // 根据客户类型查找
    List<Customer> findByCustomerType(Customer.CustomerType customerType);
    
    // 根据状态查找
    List<Customer> findByStatus(String status);
    
    // 根据客户姓名模糊搜索（分页）
    Page<Customer> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    // 根据公司名称模糊搜索（分页）
    Page<Customer> findByCompanyNameContainingIgnoreCase(String companyName, Pageable pageable);
    
    // 根据客户类型和状态查找（分页）
    Page<Customer> findByCustomerTypeAndStatus(Customer.CustomerType customerType, String status, Pageable pageable);
    
    // 根据状态查找（分页）
    Page<Customer> findByStatus(String status, Pageable pageable);
    
    // 统计活跃客户数量
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.status = 'ACTIVE'")
    long countActiveCustomers();
    
    // 统计各类型客户数量
    @Query("SELECT c.customerType, COUNT(c) FROM Customer c WHERE c.status = 'ACTIVE' GROUP BY c.customerType")
    List<Object[]> countCustomersByType();
    
    // 查找最近创建的客户
    @Query("SELECT c FROM Customer c ORDER BY c.createdAt DESC")
    Page<Customer> findRecentCustomers(Pageable pageable);
    
    // 根据客户姓名或公司名称搜索
    @Query("SELECT c FROM Customer c WHERE " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Customer> searchCustomers(@Param("keyword") String keyword, Pageable pageable);
    
    // 检查邮箱是否已存在
    @Query("SELECT COUNT(c) > 0 FROM Customer c WHERE c.email = :email AND c.customerId != :customerId")
    boolean existsByEmailExcludingId(@Param("email") String email, @Param("customerId") Long customerId);
    
    // 检查电话是否已存在
    @Query("SELECT COUNT(c) > 0 FROM Customer c WHERE c.phone = :phone AND c.customerId != :customerId")
    boolean existsByPhoneExcludingId(@Param("phone") String phone, @Param("customerId") Long customerId);
} 