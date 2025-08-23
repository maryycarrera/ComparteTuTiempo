package com.compartetutiempo.timebank.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class CreditLimitReachedException extends RuntimeException {

    public CreditLimitReachedException() {
        super("You have reached the time credit limit this month. Make more donations to increase your limit or wait until next month.");
    }

}
