package com.iyzico.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author erenadiguzel
 */

@ResponseStatus(HttpStatus.CONFLICT)
public class OutOfStockException extends Exception {
    public OutOfStockException(String message) {
        super(message);
    }
}