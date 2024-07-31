package com.tutorial.bankingservice.service;

import com.tutorial.bankingservice.dto.UserRegistrationRequest;
import com.tutorial.bankingservice.exception.BadRequestException;
import com.tutorial.bankingservice.model.User;
import com.tutorial.bankingservice.repository.UserRepository;
import com.tutorial.bankingservice.security.Role;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(UserRegistrationRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setRole(Role.valueOf(request.getRole()));

        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
