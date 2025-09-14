package com.compartetutiempo.timebank.payload.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListMessageResponse<T> {

    private String message;
    private List<T> objects;

    public ListMessageResponse(String message) {
        this.message = message;
        this.objects = null;
    }

    public ListMessageResponse(String message, List<T> objects) {
        this.message = message;
        this.objects = objects;
    }

}
