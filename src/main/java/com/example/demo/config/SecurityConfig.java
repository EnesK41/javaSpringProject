package com.example.demo.config;

import com.example.demo.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Add this import
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration; // Add this import
import org.springframework.web.cors.CorsConfigurationSource; // Add this import
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // Add this import

import java.util.Arrays; // Add this import
import java.util.List; // Add this import

@Configuration
@EnableWebSecurity // Ensure this annotation is present
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomUserDetailsService userDetailsService) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless API
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())) // Keep for H2 console
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // <<-- ADDED THIS LINE FOR CORS CONFIGURATION
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/h2-console/**").permitAll() // Allow H2 console and root
                .requestMatchers("/auth/**").permitAll() // Allow all auth endpoints without authentication
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/publisher/**").hasRole("PUBLISHER")
                .requestMatchers("/user/**").hasRole("USER")
                .anyRequest().authenticated() // All other requests require authentication
            );

        // Add the JWT filter before the UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // <<-- ADDED THIS BEAN FOR CORS CONFIGURATION
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow requests from your React application's URL
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        // Allow the HTTP methods you use (POST, GET, PUT, DELETE, OPTIONS)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Allow necessary headers, including Authorization for JWT
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        // Allow sending credentials (like cookies or HTTP authentication headers)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply this CORS configuration to all paths
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @SuppressWarnings("removal") // Suppress warning for deprecated `and()` method
    @Bean
    public AuthenticationManager authManager(HttpSecurity http, CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                       .userDetailsService(userDetailsService)
                       .passwordEncoder(passwordEncoder)
                       .and() // This 'and()' is part of the deprecated fluent API
                       .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}