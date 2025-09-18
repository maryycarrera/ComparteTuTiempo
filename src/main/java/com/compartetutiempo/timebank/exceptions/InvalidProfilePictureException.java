package com.compartetutiempo.timebank.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class InvalidProfilePictureException extends RuntimeException {

    public InvalidProfilePictureException(String color, String action) {
        super(String.format("The color %s is not a valid profile picture color for action: '%s'", color, action));
    }

    public InvalidProfilePictureException() {
        super("Invalid profile picture");
    }

    public InvalidProfilePictureException(String message) {
        super(message);
    }

}
