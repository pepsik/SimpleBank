package com.aleksandr.berezovyi.exceptions;

/**
 * Created by pepsik on 12/19/2015.
 */
public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
