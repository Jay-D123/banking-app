package com.ciicc;

import com.ciicc.config.DatabaseConnection;
import com.ciicc.dao.AccountDao;
import com.ciicc.dao.TransactionDao;
import com.ciicc.model.Account;
import com.ciicc.exception.InvalidTransactionException;
import com.ciicc.model.Transaction;
import com.ciicc.model.TransactionType;
import com.ciicc.exception.AccountNotFoundException;
import java.util.List;
import com.ciicc.exception.InsufficientBalanceException;
import java.sql.Connection;
import java.sql.SQLException;

public class BankingService {

    private final AccountDao accountDao;
    private final TransactionDao transactionDao;

    public BankingService(AccountDao accountDao, TransactionDao transactionDao) {
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
    }

    public Account createAccount(int accountNumber, String accountName, double initialDeposit)
            throws InvalidTransactionException {

        if (accountName == null || accountName.trim().isEmpty()) {
            throw new InvalidTransactionException("Account name must not be empty.");
        }

        if (initialDeposit < 0) {
            throw new InvalidTransactionException("Initial deposit must be zero or greater.");
        }

        if (accountDao.findAccountByNumber(accountNumber) != null) {
            throw new InvalidTransactionException("Account number already exists.");
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

    public Account balanceInquiry(int accountNumber) throws AccountNotFoundException {
        Account account = accountDao.findAccountByNumber(accountNumber);

        if (account == null) {
            throw new AccountNotFoundException(accountNumber);
        }

        return account;
    }

    public List<Account> listAccounts() {
        return accountDao.findAllAccounts();
    }

    private String generateReference() {
        int nextNumber = transactionDao.countTransactions() + 1;
        return String.format("TXN-%03d", nextNumber);
    }

    public Transaction deposit(int accountNumber, double amount)
            throws AccountNotFoundException, InvalidTransactionException {

        if (amount <= 0) {
            throw new InvalidTransactionException("Deposit amount must be greater than zero.");
        }

        Account account = accountDao.findAccountByNumber(accountNumber);

        if (account == null) {
            throw new AccountNotFoundException(accountNumber);
        }

        double newBalance = account.getBalance() + amount;

        boolean updated = accountDao.updateBalance(accountNumber, newBalance);

        if (!updated) {
            throw new IllegalStateException("Failed to update balance.");
        }

        Transaction transaction = new Transaction(
                generateReference(),
                accountNumber,
                TransactionType.DEPOSIT,
                amount,
                newBalance,
                null
        );

        boolean inserted = transactionDao.insertTransaction(transaction);

        if (!inserted) {
            throw new IllegalStateException("Failed to record deposit transaction.");
        }

        return transaction;
    }

    public Transaction withdraw(int accountNumber, double amount)
            throws AccountNotFoundException, InvalidTransactionException, InsufficientBalanceException {

        if (amount <= 0) {
            throw new InvalidTransactionException("Withdrawal amount must be greater than zero.");
        }

        Account account = accountDao.findAccountByNumber(accountNumber);

        if (account == null) {
            throw new AccountNotFoundException(accountNumber);
        }

        if (amount > account.getBalance()) {
            throw new InsufficientBalanceException(account.getBalance(), amount);
        }

        double newBalance = account.getBalance() - amount;

        boolean updated = accountDao.updateBalance(accountNumber, newBalance);

        if (!updated) {
            throw new IllegalStateException("Failed to update balance.");
        }

        Transaction transaction = new Transaction(
                generateReference(),
                accountNumber,
                TransactionType.WITHDRAW,
                amount,
                newBalance,
                null
        );

        boolean inserted = transactionDao.insertTransaction(transaction);

        if (!inserted) {
            throw new IllegalStateException("Failed to record withdrawal transaction.");
        }

        return transaction;
    }

    public Transaction transfer(int sourceAccountNumber, int destinationAccountNumber, double amount)
            throws AccountNotFoundException, InvalidTransactionException, InsufficientBalanceException {

        if (sourceAccountNumber == destinationAccountNumber) {
            throw new InvalidTransactionException("Source and destination accounts must be different.");
        }

        if (amount <= 0) {
            throw new InvalidTransactionException("Transfer amount must be greater than zero.");
        }

        Account source = accountDao.findAccountByNumber(sourceAccountNumber);

        if (source == null) {
            throw new AccountNotFoundException(sourceAccountNumber);
        }

        Account destination = accountDao.findAccountByNumber(destinationAccountNumber);

        if (destination == null) {
            throw new AccountNotFoundException(destinationAccountNumber);
        }

        if (amount > source.getBalance()) {
            throw new InsufficientBalanceException(source.getBalance(), amount);
        }

        String reference = generateReference();
        double sourceNewBalance = source.getBalance() - amount;
        double destinationNewBalance = destination.getBalance() + amount;

        Transaction transaction = new Transaction(
                reference,
                sourceAccountNumber,
                TransactionType.TRANSFER,
                amount,
                sourceNewBalance,
                destinationAccountNumber
        );

        try (Connection connection = DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                boolean sourceUpdated = accountDao.updateBalance(connection, sourceAccountNumber, sourceNewBalance);
                boolean destinationUpdated = accountDao.updateBalance(connection, destinationAccountNumber, destinationNewBalance);
                boolean transactionInserted = transactionDao.insertTransaction(connection, transaction);

                if (!sourceUpdated || !destinationUpdated || !transactionInserted) {
                    throw new SQLException("A transfer step did not complete.");
                }

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw new IllegalStateException("Transfer failed. No changes were made.");
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Transfer failed: " + e.getMessage());
        }

        return transaction;
    }
}