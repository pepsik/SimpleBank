package com.aleksandr.berezovyi.exceptions;

/**
 * Created by pepsik on 12/19/2015.
 */
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
