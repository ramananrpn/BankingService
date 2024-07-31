package com.tutorial.bankingservice.repository;

import com.tutorial.bankingservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountIdAndTimestampBetween(Long accountId, String startDate, String endDate);
    List<Transaction> findByAccountId(Long accountId);
}
