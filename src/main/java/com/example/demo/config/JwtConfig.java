package com.example.demo.config;

import com.example.demo.entity.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Configuration;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    // Base64-encoded secret (32+ bytes for HS256)
    private static final String SECRET = "VGhpcyBpcyBhIHZlcnkgc2VjdXJlIHNlY3JldCBmb3IgSFMgMjU2IGJpdCBsb25n";

    private static final long EXPIRATION_MS = 3600_000; // 1 hour

    public Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Account account) {
        Map<String, Object> claims = new HashMap<>(Map.of(
        "role", account.getRole().name()
        ));
        claims.put("userId", account.getId()); 
        return generateToken(account.getEmail(), claims);
    }

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

    public Claims parseToken(String token) {
        Key key = getSigningKey();
        Jws<Claims> claimsJws = Jwts.parser()  
                                    .verifyWith((SecretKey) key)  
                                    .build()
                                    .parseSignedClaims(token);    
        return claimsJws.getPayload();  
    }
}
