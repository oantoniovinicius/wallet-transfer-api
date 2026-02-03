package com.antonio.wallettransfer.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException() {
        super("Insufficient balance to complete the transfer");
    }
}
