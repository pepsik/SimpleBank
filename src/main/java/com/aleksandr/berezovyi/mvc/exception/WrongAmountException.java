package com.aleksandr.berezovyi.mvc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by pepsik on 12/25/2015.
 */
@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Amount must be > 0")
public class WrongAmountException extends RuntimeException {
}