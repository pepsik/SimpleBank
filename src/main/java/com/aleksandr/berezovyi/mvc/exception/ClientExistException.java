package com.aleksandr.berezovyi.mvc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by pepsik on 12/24/2015.
 */
@ResponseStatus(value= HttpStatus.CONFLICT, reason="Client is exist with same firstname and lastname")
public class ClientExistException extends RuntimeException {
}