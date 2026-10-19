package com.ciicc.exception;

public class AccountNotFoundException extends Exception {

    public AccountNotFoundException(int accountNumber) {
        super("Account not found: " + accountNumber);
    }
}
