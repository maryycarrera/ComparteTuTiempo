package com.compartetutiempo.timebank.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(HttpStatus.CONFLICT)
@Getter
public class AttributeDuplicatedException extends RuntimeException {

    public AttributeDuplicatedException(String attributeName, String attributeValue) {
        super(String.format("El dato '%s' con valor '%s' ya está en uso.", attributeName, attributeValue));
    }

}
