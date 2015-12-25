package com.aleksandr.berezovyi.mvc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by pepsik on 12/25/2015.
 */
@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Client name must have at least one character")
public class WrongClientNameException extends RuntimeException {
}