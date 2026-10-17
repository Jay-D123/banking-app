package com.ciicc;

import com.ciicc.dao.AccountDao;
import com.ciicc.dao.AccountDaoImpl;
import com.ciicc.dao.TransactionDao;
import com.ciicc.dao.TransactionDaoImpl;


public class BankingService {

    private final AccountDao accountDao = new AccountDaoImpl();
    private final TransactionDao transactionDao = new TransactionDaoImpl();
}
