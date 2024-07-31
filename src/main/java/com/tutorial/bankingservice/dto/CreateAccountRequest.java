package com.tutorial.bankingservice.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data
public class CreateAccountRequest {

    @NotEmpty
    private String username;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String accountType;

    @Positive
    private double initialDeposit;

    @NotEmpty
    private String aadhaar;
}
