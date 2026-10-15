package com.ciicc;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Account {
    private int id;
    private int accountNumber;
    private String accountName;
    private double balance;

    public Account(int accountNumber, String accountName, double balance) {
        this.balance = balance;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
    }
}
