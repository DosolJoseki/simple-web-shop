package com.zemliak.simplewebshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "item not found on our language")
public class ItemLocalizationNotFound extends RuntimeException{
    public ItemLocalizationNotFound() {
        super();
    }
}