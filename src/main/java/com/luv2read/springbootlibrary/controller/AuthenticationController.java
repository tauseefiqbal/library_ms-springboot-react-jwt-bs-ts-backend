package com.luv2read.springbootlibrary.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luv2read.springbootlibrary.config.JwtUtil;
import com.luv2read.springbootlibrary.dao.UserRepository;
import com.luv2read.springbootlibrary.entity.User;
import com.luv2read.springbootlibrary.requestmodels.AuthenticationRequest;
import com.luv2read.springbootlibrary.requestmodels.RegistrationRequest;
import com.luv2read.springbootlibrary.responsemodels.AuthenticationResponse;
import com.luv2read.springbootlibrary.service.CustomUserDetailsService;

@CrossOrigin({"http://localhost:3000", "http://localhost:3001", "http://localhost:3002", "http://localhost:3003", "http://localhost:5173", "http://localhost:5174", "http://localhost:5175"})
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) throws Exception {
        System.out.println("=== LOGIN REQUEST RECEIVED ===");
        System.out.println("Email: " + request.getEmail());
        System.out.println("Password length: " + (request.getPassword() != null ? request.getPassword().length() : 0));
        
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            System.out.println("Authentication successful for: " + request.getEmail());
        } catch (BadCredentialsException e) {
            System.out.println("Authentication failed for: " + request.getEmail());
            throw new Exception("Incorrect email or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new Exception("User not found"));

        return ResponseEntity.ok(new AuthenticationResponse(
                jwt,
                user.getEmail(),
                user.getName(),
                user.getIsAdmin(),
                user.getRole()
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) throws Exception {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(409).body(Map.of("error", "Email already exists"));
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setIsAdmin(false); // New users are not admins by default

        userRepository.save(user);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(
                jwt,
                user.getEmail(),
                user.getName(),
                user.getIsAdmin(),
                user.getRole()
        ));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) throws Exception {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            if (jwtUtil.validateToken(token, userDetails)) {
                User user = userRepository.findByEmail(username)
                        .orElseThrow(() -> new Exception("User not found"));
                
                return ResponseEntity.ok(new AuthenticationResponse(
                        token,
                        user.getEmail(),
                        user.getName(),
                        user.getIsAdmin(),
                        user.getRole()
                ));
            }
        }
        throw new Exception("Invalid token");
    }

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin() {
        try {
            Optional<User> existingUser = userRepository.findByEmail("admin@gmail.com");
            
            User adminUser;
            if (existingUser.isPresent()) {
                adminUser = existingUser.get();
                adminUser.setPassword(passwordEncoder.encode("admin123"));
                adminUser.setIsAdmin(true);
                adminUser.setName("Admin");
            } else {
                adminUser = new User();
                adminUser.setEmail("admin@gmail.com");
                adminUser.setPassword(passwordEncoder.encode("admin123"));
                adminUser.setName("Admin");
                adminUser.setIsAdmin(true);
            }
            
            userRepository.save(adminUser);
            
            return ResponseEntity.ok(Map.of(
                "message", "Admin user created/updated successfully",
                "email", "admin@gmail.com",
                "password", "admin123"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to create admin: " + e.getMessage()));
        }
    }
}
