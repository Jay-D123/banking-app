package com.ciicc.dao;

import com.ciicc.model.Account;
import com.ciicc.config.DatabaseConnection;

import java.util.List;

public interface AccountDao {
    boolean insertAccount(Account account);
    Account findAccountByNumber(int accountNumber);
    List<Account> findAllAccounts();
    boolean updateBalance(int accountNumber, double newBalance);
}