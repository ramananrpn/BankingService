package com.tutorial.bankingservice.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateAccountRequest {

    @NotEmpty
    private String accountType;
}
