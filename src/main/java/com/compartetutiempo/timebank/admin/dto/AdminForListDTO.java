package com.compartetutiempo.timebank.admin.dto;

import com.compartetutiempo.timebank.admin.Administrator;

import lombok.Getter;

@Getter
public class AdminForListDTO {

    private String fullName;
    private String username;
    private String email;

    public AdminForListDTO(Administrator admin) {
        this.fullName = admin.getFullName();
        this.username = admin.getUser().getUsername();
        this.email = admin.getEmail();
    }

}
