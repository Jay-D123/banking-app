package com.ciicc;

import java.util.List;

public interface AccountDao {
    boolean insertAccount(Account account);
    Account findAccountByNumber(int accountNumber);
    List<Account> findAllAccounts();
}