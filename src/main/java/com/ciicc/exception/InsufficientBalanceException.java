package com.ciicc.exception;

public class InsufficientBalanceException extends Exception {

    private final double availableBalance;
    private final double requestedAmount;

    public InsufficientBalanceException(double availableBalance, double requestedAmount) {
        super(String.format("Insufficient balance. Available: %.2f, Requested: %.2f",
                availableBalance, requestedAmount));
        this.availableBalance = availableBalance;
        this.requestedAmount = requestedAmount;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }
}