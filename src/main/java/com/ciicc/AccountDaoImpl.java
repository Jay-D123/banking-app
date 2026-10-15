package com.ciicc;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccountDaoImpl implements AccountDao {

    @Override
    public boolean insertAccount(Account account) {
        String query = "INSERT INTO accounts(account_number, account_name, balance) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {

            preparedStatement.setInt(1, account.getAccountNumber());
            preparedStatement.setString(2, account.getAccountName());
            preparedStatement.setDouble(3, account.getBalance());

            int numberOfRowsInserted = preparedStatement.executeUpdate();

            return numberOfRowsInserted > 0;

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return false;
    }

    @Override
    public Account findAccountByNumber(int accountNumber) {
        Account account = null;
        String query = "SELECT * FROM accounts WHERE account_number = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {

            preparedStatement.setInt(1, accountNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery();) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int accNumber = resultSet.getInt("account_number");
                    String accountName = resultSet.getString("account_name");
                    double balance = resultSet.getDouble("balance");
                    LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

                    account = new Account(id, accNumber, accountName, balance, createdAt);
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return account;
    }

    @Override
    public List<Account> findAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM accounts";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int accNumber = resultSet.getInt("account_number");
                String accountName = resultSet.getString("account_name");
                double balance = resultSet.getDouble("balance");
                LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

                Account account = new Account(id, accNumber, accountName, balance, createdAt);
                accounts.add(account);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return accounts;
    }
}