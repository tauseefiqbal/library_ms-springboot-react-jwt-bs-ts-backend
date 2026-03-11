package com.luv2read.springbootlibrary.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String password1 = "admin123";
        String password2 = "password123";
        String password3 = "test123";
        
        System.out.println("BCrypt hashes:");
        System.out.println("===============");
        System.out.println("Password: " + password1);
        System.out.println("Hash: " + encoder.encode(password1));
        System.out.println();
        System.out.println("Password: " + password2);
        System.out.println("Hash: " + encoder.encode(password2));
        System.out.println();
        System.out.println("Password: " + password3);
        System.out.println("Hash: " + encoder.encode(password3));
        System.out.println();
        System.out.println("Copy one of these hashes and use it in your SQL UPDATE statement.");
    }
}
