package com.tutorial.bankingservice.controller;

import com.tutorial.bankingservice.dto.UserRegistrationRequest;
import com.tutorial.bankingservice.model.User;
import com.tutorial.bankingservice.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationRequest user) {
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        User savedUser = userService.saveUser(user);
        log.info("User {} registered successfully", savedUser.getId());
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
}
