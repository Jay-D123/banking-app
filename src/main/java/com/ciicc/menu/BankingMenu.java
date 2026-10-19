package com.ciicc.menu;

import com.ciicc.BankingService;
import java.util.Scanner;
import com.ciicc.exception.InvalidTransactionException;
import com.ciicc.model.Account;
import com.ciicc.exception.AccountNotFoundException;
import java.util.List;
import com.ciicc.model.Transaction;
import com.ciicc.exception.InsufficientBalanceException;

public class BankingMenu {

    private final BankingService bankingService;
    private final Scanner scanner = new Scanner(System.in);

    public BankingMenu(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    public void start() {
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createAccount();
                    break;
                case "2":
                    balanceInquiry();
                    break;
                case "3":
                    listAccounts();
                    break;
                case "4":
                    deposit();
                    break;
                case "5":
                    withdraw();
                    break;
                case "6":
                    transfer();
                    break;
                case "0":
                    running = false;
                    System.out.println("Thank you for choosing CIICC Bank. Have a great day!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("");
        System.out.println(" BANKING APPLICATION");
        System.out.println("");
        System.out.println("1. Create Account");
        System.out.println("2. Balance Inquiry");
        System.out.println("3. List Accounts");
        System.out.println("4. Deposit");
        System.out.println("5. Withdraw");
        System.out.println("6. Transfer");
        System.out.println("7. Transaction History");
        System.out.println("0. Exit");
    }

    private void createAccount() {
        System.out.println();
        System.out.println("------ CREATE ACCOUNT ------");

        try {
            System.out.print("Enter account number: ");
            int accountNumber = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter account holder name: ");
            String accountName = scanner.nextLine();

            System.out.print("Enter initial deposit: ");
            double initialDeposit = Double.parseDouble(scanner.nextLine().trim());

            Account account = bankingService.createAccount(accountNumber, accountName, initialDeposit);

            System.out.println();
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Account Name: " + account.getAccountName());
            System.out.printf("Initial Deposit: ₱%,.2f%n", account.getBalance());
            System.out.println("Account created successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid number.");
        } catch (InvalidTransactionException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private void balanceInquiry() {
        System.out.println();
        System.out.println("------ BALANCE INQUIRY ------");

        try {
            System.out.print("Enter account number: ");
            int accountNumber = Integer.parseInt(scanner.nextLine().trim());

            Account account = bankingService.balanceInquiry(accountNumber);

            System.out.println();
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Account Holder: " + account.getAccountName());
            System.out.printf("Current Balance: ₱%,.2f%n", account.getBalance());

        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid number.");
        } catch (AccountNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private void listAccounts() {
        System.out.println();
        System.out.println("------ LIST ACCOUNTS ------");

        List<Account> accounts = bankingService.listAccounts();

        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        for (Account account : accounts) {
            System.out.printf("%-10d %-25s ₱%,.2f%n",
                    account.getAccountNumber(),
                    account.getAccountName(),
                    account.getBalance());
        }

        System.out.println();
        System.out.println("Total accounts: " + accounts.size());
    }

    private void deposit() {
        System.out.println();
        System.out.println("------ DEPOSIT ------");

        try {
            System.out.print("Enter account number: ");
            int accountNumber = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter deposit amount: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            Transaction transaction = bankingService.deposit(accountNumber, amount);

            double previousBalance = transaction.getBalance() - transaction.getAmount();

            System.out.println();
            System.out.printf("Previous Balance: ₱%,.2f%n", previousBalance);
            System.out.printf("Deposit Amount: ₱%,.2f%n", transaction.getAmount());
            System.out.printf("New Balance: ₱%,.2f%n", transaction.getBalance());
            System.out.println("Deposit successful!");

        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid number.");
        } catch (AccountNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidTransactionException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void withdraw() {
        System.out.println();
        System.out.println("------ WITHDRAW ------");

        try {
            System.out.print("Enter account number: ");
            int accountNumber = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter withdrawal amount: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            Transaction transaction = bankingService.withdraw(accountNumber, amount);

            double previousBalance = transaction.getBalance() + transaction.getAmount();

            System.out.println();
            System.out.printf("Previous Balance: ₱%,.2f%n", previousBalance);
            System.out.printf("Withdrawal Amount: ₱%,.2f%n", transaction.getAmount());
            System.out.printf("New Balance: ₱%,.2f%n", transaction.getBalance());
            System.out.println("Withdrawal successful!");

        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid number.");
        } catch (AccountNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidTransactionException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InsufficientBalanceException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void transfer() {
        System.out.println();
        System.out.println("------ TRANSFER ------");

        try {
            System.out.print("Enter source account number: ");
            int sourceAccountNumber = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter destination account number: ");
            int destinationAccountNumber = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter transfer amount: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            Transaction transaction = bankingService.transfer(
                    sourceAccountNumber, destinationAccountNumber, amount);

            System.out.println();
            System.out.println("From Account: " + transaction.getAccountNumber());
            System.out.println("To Account: " + transaction.getReferenceAccount());
            System.out.printf("Amount: ₱%,.2f%n", transaction.getAmount());
            System.out.println("Transfer successful!");

        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter a valid number.");
        } catch (AccountNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidTransactionException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InsufficientBalanceException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}