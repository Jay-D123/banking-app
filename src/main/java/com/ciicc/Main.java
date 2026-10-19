package com.ciicc;

import com.ciicc.dao.AccountDao;
import com.ciicc.dao.AccountDaoImpl;
import com.ciicc.dao.TransactionDao;
import com.ciicc.dao.TransactionDaoImpl;
import com.ciicc.menu.BankingMenu;

public class Main {

    public static void main(String[] args) {
        AccountDao accountDao = new AccountDaoImpl();
        TransactionDao transactionDao = new TransactionDaoImpl();

        BankingService bankingService = new BankingService(accountDao, transactionDao);
        BankingMenu menu = new BankingMenu(bankingService);

        menu.start();
    }
}