package com.example.ExpenseTracker.Security;

import com.example.ExpenseTracker.Repository.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {

    @Value("${spring.jwt.secret-key}")
    private String Secret_Key;
    @Value("${spring.jwt.expiration}")
    private long expiration;

    public SecretKey generateKey() {
        return Keys.hmacShaKeyFor(Secret_Key.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email) {
        log.info("generate token!!");
        return Jwts.builder()
                .setSubject(email)
                .signWith(generateKey())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(generateKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public boolean validationToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }

}
