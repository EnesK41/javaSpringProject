package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.AuthenticationException;


import com.example.demo.config.JwtConfig;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.CustomUserDetailsService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtConfig jwtConfig;
    private final CustomUserDetailsService userDetailsService;
    private final AccountRepository accountRepository;
    private final UserProfileRepository userProfileRepository;
    private final PublisherProfileRepository publisherProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authManager,
                          JwtConfig jwtConfig,
                          CustomUserDetailsService userDetailsService,
                          AccountRepository accountRepository,
                          UserProfileRepository userProfileRepository,
                          PublisherProfileRepository publisherProfileRepository,
                          PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.jwtConfig = jwtConfig;
        this.userDetailsService = userDetailsService;
        this.accountRepository = accountRepository;
        this.userProfileRepository = userProfileRepository;
        this.publisherProfileRepository = publisherProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO request) {
        System.out.println("Register attempt for: " + request.getEmail() + ", role: " + request.getRole());
        // Only check User and Publisher
        if(accountRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(400).body("Email already exists");
        }

        Account account = new Account();
        account.setName(request.getName());
        account.setEmail(request.getEmail());
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        if(request.getRole() != null && request.getRole().equalsIgnoreCase("PUBLISHER")) {
            account.setRole(Account.Role.PUBLISHER);
            accountRepository.save(account);

            PublisherProfile profile = new PublisherProfile();
            profile.setAccount(account);
            profile.setPoints(0);
            publisherProfileRepository.save(profile);

        } else { // Default USER
            account.setRole(Account.Role.USER);
            accountRepository.save(account);

            UserProfile profile = new UserProfile();
            profile.setAccount(account);
            profile.setPoints(0);
            userProfileRepository.save(profile);
        }

        String token = jwtConfig.generateToken(account);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("role", account.getRole().name());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request) {
        // Authenticate user
        System.out.println("Login attempt for: " + request.getEmail());
        try {
            authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        // Load Account details
        CustomUserDetails customUser = (CustomUserDetails) userDetailsService.loadUserByUsername(request.getEmail());
        Account account = customUser.getAccount();

        // Generate JWT token
        String token = jwtConfig.generateToken(account);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("role", account.getRole().name());
        return ResponseEntity.ok(response);
    }
}
