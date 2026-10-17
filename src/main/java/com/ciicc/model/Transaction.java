package com.ciicc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor

public class Transaction {
    private int id;
    private String transactionReference;
    private int accountNumber;
    private TransactionType transactionType;
    private double amount;
    private double balance;
    private Integer referenceAccount;
    private LocalDateTime createdAt;

    public Transaction(String transactionReference, int accountNumber, TransactionType transactionType,
                      double amount, double balance, Integer referenceAccount) {
        this.transactionReference = transactionReference;
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balance = balance;
        this.referenceAccount = referenceAccount;
    }
}
