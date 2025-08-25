package com.example.demo.config;

import com.example.demo.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtConfig jwtConfig, CustomUserDetailsService userDetailsService) {
        this.jwtConfig = jwtConfig;
        this.userDetailsService = userDetailsService;
    }

    // --- ADDED THIS METHOD ---
    // This method ensures that the JWT filter does NOT run for authentication endpoints.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // We only want to filter requests that are not for authentication (login/register)
        return request.getServletPath().startsWith("/auth");
    }
    // -------------------------

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // If shouldNotFilter returns true, this doFilterInternal method won't even be called for those paths.
        // However, the original code remains valid for requests that *should* be filtered.
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Claims claims = jwtConfig.parseToken(token); // Make sure parseToken handles invalid tokens gracefully (e.g., throws an exception caught by a handler)
            String username = claims.getSubject();

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                var userDetails = userDetailsService.loadUserByUsername(username);

                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
