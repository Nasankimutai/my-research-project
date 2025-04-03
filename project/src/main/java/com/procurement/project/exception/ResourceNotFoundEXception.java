package com.procurement.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundEXception extends RuntimeException {
    public ResourceNotFoundEXception(String message) {
        super(message);
    }
}
