package com.tutorial.bankingservice.controller;

import com.tutorial.bankingservice.dto.CreateAccountRequest;
import com.tutorial.bankingservice.dto.UpdateAccountRequest;
import com.tutorial.bankingservice.dto.response.AccountResponse;
import com.tutorial.bankingservice.dto.response.BalanceResponse;
import com.tutorial.bankingservice.dto.response.StatementResponse;
import com.tutorial.bankingservice.model.Account;
import com.tutorial.bankingservice.service.AccountService;
import com.tutorial.bankingservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest) {
        Account account = accountService.createAccount(createAccountRequest);
        AccountResponse response = AccountResponse.builder()
                .accountId(account.getId())
                .accountType(account.getAccountType())
                .aadhaar(account.getAadhaar())
                .balance(account.getBalance())
                .username(account.getUsername())
                .email(account.getEmail())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable Long id, @Valid @RequestBody UpdateAccountRequest updateAccountRequest) {
        Account account = accountService.updateAccount(id, updateAccountRequest);
        AccountResponse response = AccountResponse.builder()
                .accountId(account.getId())
                .accountType(account.getAccountType())
                .aadhaar(account.getAadhaar())
                .balance(account.getBalance())
                .username(account.getUsername())
                .email(account.getEmail())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable Long id) {
        double balance = accountService.getBalance(id);
        BalanceResponse response = new BalanceResponse(id, balance);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}/statements")
    public ResponseEntity<StatementResponse> getStatements(@PathVariable Long id,
                                                           @RequestParam String startDate,
                                                           @RequestParam String endDate) {
        List<Transaction> statements = transactionService.getStatements(id, startDate, endDate);
        StatementResponse response = new StatementResponse(id, statements, startDate, endDate);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable Long id) {
        List<Transaction> transactions = transactionService.getTransactions(id);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
