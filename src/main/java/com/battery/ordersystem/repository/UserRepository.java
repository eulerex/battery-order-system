package com.battery.ordersystem.repository;

import com.battery.ordersystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
} 