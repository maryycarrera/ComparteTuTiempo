package com.compartetutiempo.timebank.auth.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {

    private String message;
    private Object object;

    public MessageResponse(String message) {
        this.message = message;
        this.object = null;
    }

    public MessageResponse(String message, Object object) {
        this.message = message;
        this.object = object;
    }

}
