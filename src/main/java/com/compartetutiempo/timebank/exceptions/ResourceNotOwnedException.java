package com.compartetutiempo.timebank.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class ResourceNotOwnedException extends RuntimeException {

    public ResourceNotOwnedException(final Object object) {
        super(String.format("Resource '%s' is not owned by the user.", object.getClass().getSimpleName()));
    }

}
