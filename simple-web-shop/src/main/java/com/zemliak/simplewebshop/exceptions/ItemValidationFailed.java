package com.zemliak.simplewebshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "item validation failed")
public class ItemValidationFailed extends RuntimeException{
    public ItemValidationFailed() {
        super();
    }
}