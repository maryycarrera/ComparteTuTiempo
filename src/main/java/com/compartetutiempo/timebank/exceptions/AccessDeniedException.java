package com.compartetutiempo.timebank.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(HttpStatus.FORBIDDEN)
@Getter
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String resourceName, String action) {
        super(String.format("Access denied to %s for action: '%s'", resourceName, action));
    }

    public AccessDeniedException() {
        super("Access denied");
    }

    public AccessDeniedException(String message) {
        super(message);
    }

}
