package com.luv2read.springbootlibrary.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luv2read.springbootlibrary.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
}
