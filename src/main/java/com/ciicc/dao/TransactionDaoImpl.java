package com.ciicc.dao;

import com.ciicc.config.DatabaseConnection;
import com.ciicc.model.Transaction;
import com.ciicc.model.TransactionType;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDaoImpl implements TransactionDao {

    @Override
    public boolean insertTransaction(Transaction transaction) {
        String query = "INSERT INTO transactions(transaction_reference, account_number, transaction_type, amount, balance, reference_account) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {

        preparedStatement.setString(1, transaction.getTransactionReference());
        preparedStatement.setInt(2, transaction.getAccountNumber());
        preparedStatement.setString(3, transaction.getTransactionType().name());
        preparedStatement.setDouble(4, transaction.getAmount());
        preparedStatement.setDouble(5, transaction.getBalance());

        if (transaction.getReferenceAccount() != null) {
            preparedStatement.setInt(6, transaction.getReferenceAccount());
        }else{
            preparedStatement.setInt(6, Types.INTEGER);
        }

            int numberOfRowsInserted = preparedStatement.executeUpdate();

            return numberOfRowsInserted > 0;

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return false;
    }

    @Override
    public
    List<Transaction> findTransactionsByAccount(int accountNumber) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE account_number = ? ORDER BY id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setInt(1, accountNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery();) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String transactionReference = resultSet.getString("transaction_reference");
                    int accNumber = resultSet.getInt("account_number");
                    TransactionType transactionType = TransactionType.valueOf(resultSet.getString("transaction_type"));
                    double amount = resultSet.getDouble("amount");
                    double balance = resultSet.getDouble("balance");

                    Integer referenceAccount = resultSet.getInt("reference_account");
                    if (resultSet == null) {
                        referenceAccount = null;
                    }
                    LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

                    Transaction transaction = new Transaction(id, transactionReference, accNumber, transactionType, amount, balance, referenceAccount, createdAt);
                    transactions.add(transaction);
                }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return transactions;
        }

    @Override
    public int countTransactions() {
        String query = "SELECT count(*) FROM transactions";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ){
            if(resultSet.next()){
                return resultSet.getInt(1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public boolean insertTransaction(Connection connection, Transaction transaction) throws SQLException {
        String query = "INSERT INTO transactions(transaction_reference, account_number, transaction_type, amount, balance, reference_account) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, transaction.getTransactionReference());
            preparedStatement.setInt(2, transaction.getAccountNumber());
            preparedStatement.setString(3, transaction.getTransactionType().name());
            preparedStatement.setDouble(4, transaction.getAmount());
            preparedStatement.setDouble(5, transaction.getBalance());

            if (transaction.getReferenceAccount() != null) {
                preparedStatement.setInt(6, transaction.getReferenceAccount());
            } else {
                preparedStatement.setNull(6, Types.INTEGER);
            }

            int numberOfRowsInserted = preparedStatement.executeUpdate();

            return numberOfRowsInserted > 0;
        }
    }
}
