package com.aleksandr.berezovyi.service.Exception;

/**
 * Created by pepsik on 12/19/2015.
 */
public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
