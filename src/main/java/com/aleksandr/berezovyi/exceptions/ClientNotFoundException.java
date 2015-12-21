package com.aleksandr.berezovyi.exceptions;

/**
 * Created by pepsik on 12/19/2015.
 */
public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(String message) {
        super(message);
    }
}
