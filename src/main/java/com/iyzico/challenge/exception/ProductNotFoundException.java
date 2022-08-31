package com.iyzico.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author erenadiguzel
 */

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Product not found!")
public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String message) {
        super(message);
    }
}