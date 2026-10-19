package com.ciicc.menu;

import com.ciicc.BankingService;

import java.util.Scanner;

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
                case "0":
                    running = false;
                    System.out.println("Thank you for using the Banking Application. Goodbye!");
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
}