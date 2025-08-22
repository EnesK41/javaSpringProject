package com.example.demo.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import org.springframework.context.annotation.Configuration;

import com.example.demo.entity.Account;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    // Base64-encoded secret (32+ bytes for HS256)
    private static final String SECRET = "VGhpcyBpcyBhIHZlcnkgc2VjdXJlIHNlY3JldCBmb3IgSFMgMjU2IGJpdCBsb25n"; 

    private static final long EXPIRATION_MS = 3600_000; // 1 hour

    // Get signing key
    public Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Account account) {
        Map<String, Object> claims = Map.of(
            "role", account.getRole().name() // just use the enum
        );
        return generateToken(account.getName(), claims);
    }

    // Generate token
    public String generateToken(String username, Map<String, Object> extraClaims) {
        Key key = getSigningKey();
        return Jwts.builder()
                .claims(extraClaims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key)
                .compact();
    }

    // Parse token
    public Claims parseToken(String token) {
        Key key = getSigningKey();
        return Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(token).getPayload();
    }
}
