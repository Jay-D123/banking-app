package com.ciicc;

import com.ciicc.dao.AccountDao;
import com.ciicc.dao.TransactionDao;

public class BankingService {

    private final AccountDao accountDao;
    private final TransactionDao transactionDao;

    public BankingService(AccountDao accountDao, TransactionDao transactionDao) {
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
    }
}