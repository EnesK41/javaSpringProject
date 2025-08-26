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

    // These are the dependencies you actually need for this controller.
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final AuthService authService;
    private final AccountRepository accountRepository;

    // The constructor is now cleaner, with unused dependencies removed.
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
            // This now correctly catches the custom "EmailAlreadyExistsException"
            // and returns its specific message.
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request) {
        try {
            // Let Spring Security handle the authentication
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // Find the full account details to generate the token
            Account account = accountRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found after successful authentication."));

            // Generate the token
            String token = jwtConfig.generateToken(account);

            // Return the token in the response
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            // THE FIX: We now specifically catch AuthenticationException.
            // This is the correct exception for bad credentials and prevents a 500 error.
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }
}