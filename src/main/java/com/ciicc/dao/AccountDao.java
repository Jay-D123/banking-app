package com.ciicc.dao;

import com.ciicc.model.Account;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface AccountDao {
    boolean insertAccount(Account account);
    Account findAccountByNumber(int accountNumber);
    List<Account> findAllAccounts();
    boolean updateBalance(int accountNumber, double newBalance);


    boolean updateBalance(Connection connection, int accountNumber, double newBalance) throws SQLException;
    Account findAccountByNumber(Connection connection, int accountNumber) throws SQLException;
}