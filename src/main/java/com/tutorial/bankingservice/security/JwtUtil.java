package com.tutorial.bankingservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.tutorial.bankingservice.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${spring.security.jwt.secret}")
    private String jwtSecret;

    @Value("${spring.security.jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(User user) {
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("role", user.getRole().name())
                .withClaim("email", user.getEmail())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpiration))
                .sign(Algorithm.HMAC512(jwtSecret));
    }
}
