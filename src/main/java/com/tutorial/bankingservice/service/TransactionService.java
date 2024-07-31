package com.tutorial.bankingservice.service;

import com.tutorial.bankingservice.model.Transaction;
import com.tutorial.bankingservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getStatements(Long accountId, String startDate, String endDate) {
        // Logic to retrieve and filter transactions between startDate and endDate
        return transactionRepository.findByAccountIdAndTimestampBetween(accountId, startDate, endDate);
    }

    public List<Transaction> getTransactions(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
}
