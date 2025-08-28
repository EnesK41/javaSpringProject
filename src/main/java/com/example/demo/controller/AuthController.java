package com.example.demo.controller;

import com.example.demo.config.JwtConfig;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import com.example.demo.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final AuthService authService;
    private final AccountRepository accountRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtConfig jwtConfig,
                          AuthService authService,
                          AccountRepository accountRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.authService = authService;
        this.accountRepository = accountRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO request) {
        try {
            Account newAccount = authService.registerNewUser(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getRole()
            );

            String token = jwtConfig.generateToken(newAccount);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("role", newAccount.getRole().name());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            Account account = accountRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found after successful authentication."));

            String token = jwtConfig.generateToken(account);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }
}