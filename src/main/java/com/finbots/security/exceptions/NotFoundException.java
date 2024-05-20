package com.finbots.security.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundException extends ResponseStatusException {
    private final String customMessage;

    public NotFoundException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
        this.customMessage = reason;
    }

    @Override
    public String getMessage() {
        return customMessage;
    }
}
