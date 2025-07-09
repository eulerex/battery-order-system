package com.battery.ordersystem.service;

import com.battery.ordersystem.dto.CustomerRequest;
import com.battery.ordersystem.dto.CustomerResponse;
import com.battery.ordersystem.entity.Customer;
import com.battery.ordersystem.repository.CustomerRepository;
import com.battery.ordersystem.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    // 创建客户
    public CustomerResponse createCustomer(CustomerRequest request) {
        // 验证必填字段
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("客户姓名不能为空");
        }
        
        // 检查邮箱是否已存在
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new IllegalArgumentException("邮箱已存在");
            }
        }
        
        // 检查电话是否已存在
        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            if (customerRepository.findByPhone(request.getPhone()).isPresent()) {
                throw new IllegalArgumentException("电话已存在");
            }
        }
        
        Customer customer = new Customer();
        customer.setName(request.getName().trim());
        customer.setPhone(request.getPhone() != null ? request.getPhone().trim() : null);
        customer.setEmail(request.getEmail() != null ? request.getEmail().trim() : null);
        customer.setAddress(request.getAddress() != null ? request.getAddress().trim() : null);
        customer.setCompanyName(request.getCompanyName() != null ? request.getCompanyName().trim() : null);
        customer.setContactPerson(request.getContactPerson() != null ? request.getContactPerson().trim() : null);
        customer.setNotes(request.getNotes() != null ? request.getNotes().trim() : null);
        
        // 设置客户类型
        if (request.getCustomerType() != null) {
            try {
                customer.setCustomerType(Customer.CustomerType.valueOf(request.getCustomerType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("无效的客户类型");
            }
        }
        
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerResponse.fromEntity(savedCustomer);
    }
    
    // 获取客户列表（分页）
    public Page<CustomerResponse> getCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers = customerRepository.findAll(pageable);
        return customers.map(CustomerResponse::fromEntity);
    }
    
    // 根据ID获取客户
    public Optional<CustomerResponse> getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> {
                    CustomerResponse response = CustomerResponse.fromEntity(customer);
                    // 获取订单统计信息
                    Long orderCount = orderRepository.countByName(customer.getName());
                    Double totalAmount = orderRepository.sumTotalAmountByName(customer.getName());
                    response.setOrderCount(orderCount);
                    response.setTotalAmount(totalAmount != null ? totalAmount : 0.0);
                    return response;
                });
    }
    
    // 更新客户信息
    public Optional<CustomerResponse> updateCustomer(Long customerId, CustomerRequest request) {
        return customerRepository.findById(customerId).map(customer -> {
            // 验证必填字段
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("客户姓名不能为空");
            }
            
            // 检查邮箱是否已存在（排除当前客户）
            if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                if (customerRepository.existsByEmailExcludingId(request.getEmail().trim(), customerId)) {
                    throw new IllegalArgumentException("邮箱已存在");
                }
            }
            
            // 检查电话是否已存在（排除当前客户）
            if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
                if (customerRepository.existsByPhoneExcludingId(request.getPhone().trim(), customerId)) {
                    throw new IllegalArgumentException("电话已存在");
                }
            }
            
            customer.setName(request.getName().trim());
            customer.setPhone(request.getPhone() != null ? request.getPhone().trim() : null);
            customer.setEmail(request.getEmail() != null ? request.getEmail().trim() : null);
            customer.setAddress(request.getAddress() != null ? request.getAddress().trim() : null);
            customer.setCompanyName(request.getCompanyName() != null ? request.getCompanyName().trim() : null);
            customer.setContactPerson(request.getContactPerson() != null ? request.getContactPerson().trim() : null);
            customer.setNotes(request.getNotes() != null ? request.getNotes().trim() : null);
            
            // 设置客户类型
            if (request.getCustomerType() != null) {
                try {
                    customer.setCustomerType(Customer.CustomerType.valueOf(request.getCustomerType().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("无效的客户类型");
                }
            }
            
            Customer updatedCustomer = customerRepository.save(customer);
            return CustomerResponse.fromEntity(updatedCustomer);
        });
    }
    
    // 删除客户
    public boolean deleteCustomer(Long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            // 检查是否有相关订单
            Long orderCount = orderRepository.countByName(customer.get().getName());
            if (orderCount > 0) {
                throw new IllegalArgumentException("该客户有相关订单，无法删除");
            }
            customerRepository.deleteById(customerId);
            return true;
        }
        return false;
    }
    
    // 搜索客户
    public Page<CustomerResponse> searchCustomers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers = customerRepository.searchCustomers(keyword, pageable);
        return customers.map(CustomerResponse::fromEntity);
    }
    
    // 根据客户类型筛选
    public Page<CustomerResponse> getCustomersByType(String customerType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Customer.CustomerType type = Customer.CustomerType.valueOf(customerType.toUpperCase());
            Page<Customer> customers = customerRepository.findByCustomerTypeAndStatus(type, "ACTIVE", pageable);
            return customers.map(CustomerResponse::fromEntity);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("无效的客户类型");
        }
    }
    
    // 根据状态筛选
    public Page<CustomerResponse> getCustomersByStatus(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers = customerRepository.findByStatus(status, pageable);
        return customers.map(CustomerResponse::fromEntity);
    }
    
    // 获取最近客户
    public Page<CustomerResponse> getRecentCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers = customerRepository.findRecentCustomers(pageable);
        return customers.map(CustomerResponse::fromEntity);
    }
    
    // 获取客户统计信息
    public Map<String, Object> getCustomerStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 总客户数
        long totalCustomers = customerRepository.count();
        stats.put("totalCustomers", totalCustomers);
        
        // 活跃客户数
        long activeCustomers = customerRepository.countActiveCustomers();
        stats.put("activeCustomers", activeCustomers);
        
        // 各类型客户数量
        List<Object[]> customersByType = customerRepository.countCustomersByType();
        Map<String, Long> typeStats = new HashMap<>();
        for (Object[] result : customersByType) {
            Customer.CustomerType type = (Customer.CustomerType) result[0];
            Long count = (Long) result[1];
            typeStats.put(type.getDisplayName(), count);
        }
        stats.put("customersByType", typeStats);
        
        return stats;
    }
    
    // 更新客户状态
    public Optional<CustomerResponse> updateCustomerStatus(Long customerId, String status) {
        return customerRepository.findById(customerId).map(customer -> {
            customer.setStatus(status);
            Customer updatedCustomer = customerRepository.save(customer);
            return CustomerResponse.fromEntity(updatedCustomer);
        });
    }
} 