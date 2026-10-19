package com.ciicc.dao;

import com.ciicc.model.Transaction;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface TransactionDao {
    boolean insertTransaction(Transaction transaction);
    List<Transaction> findTransactionsByAccount(int accountNumber);

    int countTransactions();

    boolean insertTransaction(Connection connection, Transaction transaction) throws SQLException;
}
