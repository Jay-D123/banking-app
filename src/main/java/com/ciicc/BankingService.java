package com.ciicc;

import com.ciicc.dao.AccountDao;
import com.ciicc.dao.TransactionDao;
import com.ciicc.model.Account;
import com.ciicc.exception.InvalidTransactionException;
import com.ciicc.model.Transaction;
import com.ciicc.model.TransactionType;

public class BankingService {

    private final AccountDao accountDao;
    private final TransactionDao transactionDao;

    public BankingService(AccountDao accountDao, TransactionDao transactionDao) {
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
    }

    public Account createAccount(int accountNumber, String accountName, double initialDeposit) {

        if (accountName == null || accountName.trim().isEmpty()) {
            throw new IllegalArgumentException("Account name must not be empty.");
        }

        if (initialDeposit < 0) {
            throw new IllegalArgumentException("Initial deposit must be zero or greater.");
        }

        if (accountDao.findAccountByNumber(accountNumber) != null) {
            throw new IllegalArgumentException("Account number already exists.");
        }

        Account account = new Account(accountNumber, accountName.trim(), initialDeposit);
        boolean inserted = accountDao.insertAccount(account);

        if (!inserted) {
            throw new IllegalStateException("Failed to create account.");
        }

        if (initialDeposit > 0) {
            Transaction transaction = new Transaction(
                    generateReference(),
                    accountNumber,
                    TransactionType.DEPOSIT,
                    initialDeposit,
                    initialDeposit,
                    null
            );

            boolean transactionInserted = transactionDao.insertTransaction(transaction);

            if (!transactionInserted) {
                throw new IllegalStateException("Failed to record initial deposit.");
            }
        }

        return accountDao.findAccountByNumber(accountNumber);
    }

    private String generateReference() {
        int nextNumber = transactionDao.countTransactions() + 1;
        return String.format("TXN-%03d", nextNumber);
    }
}