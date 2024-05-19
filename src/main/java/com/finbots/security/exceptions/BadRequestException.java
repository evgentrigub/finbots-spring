package com.finbots.security.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadRequestException extends ResponseStatusException {
    private final String customMessage;

    public BadRequestException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
        this.customMessage = reason;
    }

    @Override
    public String getMessage() {
        return customMessage;
    }
}
