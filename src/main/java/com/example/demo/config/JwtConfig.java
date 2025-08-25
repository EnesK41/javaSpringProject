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
        Map<String, Object> claims = Map.of(
            "role", account.getRole().name()
        );
        return generateToken(account.getName(), claims);
    }

    public String generateToken(String username, Map<String, Object> extraClaims) {
        Key key = getSigningKey();
        return Jwts.builder()
                .claims(extraClaims)  // Use claims() instead of setClaims()
                .subject(username)    // Use subject() instead of setSubject()
                .issuedAt(new Date()) // Use issuedAt() instead of setIssuedAt()
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS)) // Use expiration() instead of setExpiration()
                .signWith(key)
                .compact();
    }

    public Claims parseToken(String token) {
        Key key = getSigningKey();
        Jws<Claims> claimsJws = Jwts.parser()  // Use parser() instead of parserBuilder()
                                    .verifyWith((SecretKey) key)  // Use verifyWith() instead of setSigningKey()
                                    .build()
                                    .parseSignedClaims(token);    // Use parseSignedClaims() instead of parseClaimsJws()
        return claimsJws.getPayload();  // Use getPayload() instead of getBody()
    }
}
