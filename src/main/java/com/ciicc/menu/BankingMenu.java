package com.ciicc.menu;

import com.ciicc.BankingService;
import java.util.Scanner;
import com.ciicc.exception.InvalidTransactionException;
import com.ciicc.model.Account;
import com.ciicc.exception.AccountNotFoundException;

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
}