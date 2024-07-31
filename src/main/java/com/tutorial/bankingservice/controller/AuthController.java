package com.tutorial.bankingservice.controller;

import com.tutorial.bankingservice.dto.LoginRequest;
import com.tutorial.bankingservice.dto.response.JwtResponse;
import com.tutorial.bankingservice.exception.BadRequestException;
import com.tutorial.bankingservice.model.User;
import com.tutorial.bankingservice.security.JwtUtil;
import com.tutorial.bankingservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${spring.security.jwt.secret}")
    private String secretKey;

    @Value("${spring.security.jwt.expiration}")
    private long expirationTime;

    private final JwtUtil jwtUtil;

    private final UserService userService;

    public AuthController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userService.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (user.getPassword() == null || !user.getPassword().equals(loginRequest.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String jwtToken = jwtUtil.generateToken(user);
        return ResponseEntity.ok(new JwtResponse(jwtToken));
    }
}