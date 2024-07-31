package com.tutorial.bankingservice.service;

import com.tutorial.bankingservice.dto.CreateAccountRequest;
import com.tutorial.bankingservice.dto.UpdateAccountRequest;
import com.tutorial.bankingservice.exception.BadRequestException;
import com.tutorial.bankingservice.model.Account;
import com.tutorial.bankingservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(CreateAccountRequest createAccountRequest) {
        // Validate initial deposit amount
        if (createAccountRequest.getInitialDeposit() < 0) {
            throw new BadRequestException("Initial deposit amount cannot be negative");
        }

        // Validate Aadhaar number format and uniqueness
        if (!createAccountRequest.getAadhaar().matches("\\d{4}-\\d{4}-\\d{4}")) {
            throw new BadRequestException("Invalid Aadhaar number format");
        }

        Optional<Account> existingAccount = accountRepository.findByAadhaar(createAccountRequest.getAadhaar());
        if (existingAccount.isPresent()) {
            throw new BadRequestException("Aadhaar number already exists");
        }

        Account account = Account.builder()
                .accountType(createAccountRequest.getAccountType())
                .aadhaar(createAccountRequest.getAadhaar())
                .balance(createAccountRequest.getInitialDeposit())
                .username(createAccountRequest.getUsername())
                .email(createAccountRequest.getEmail())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, UpdateAccountRequest updateAccountRequest) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Account not found"));

        // Validate account type change constraints
        // (Add your specific validation logic here)

        account.setAccountType(updateAccountRequest.getAccountType());
        return accountRepository.save(account);
    }

    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Account not found"));

        // Perform cleanup operations asynchronously
        // (Add your specific cleanup logic here)

        accountRepository.delete(account);
    }

    public double getBalance(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Account not found"));
        return account.getBalance();
    }
}
