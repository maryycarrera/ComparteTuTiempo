package com.compartetutiempo.timebank.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse<T> {

    private String message;
    private T object;

    public MessageResponse(String message) {
        this.message = message;
        this.object = null;
    }

    public MessageResponse(String message, T object) {
        this.message = message;
        this.object = object;
    }

}
