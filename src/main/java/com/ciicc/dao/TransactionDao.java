package com.ciicc.dao;

import com.ciicc.model.Transaction;

import java.util.List;

public interface TransactionDao {
    boolean insertTransaction(Transaction transaction);
    List<Transaction> findTransactionByAccount (int accountNumber);
}
