package com.tutorial.bankingservice.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String password;
}
