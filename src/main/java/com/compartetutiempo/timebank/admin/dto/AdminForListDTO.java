package com.compartetutiempo.timebank.admin.dto;

import com.compartetutiempo.timebank.admin.Administrator;

import lombok.Getter;

@Getter
public class AdminForListDTO {

    private String id;
    private String fullName;
    private String username;
    private String email;

    public AdminForListDTO(Administrator admin) {
        this.id = String.valueOf(admin.getId());
        this.fullName = admin.getFullName();
        this.username = admin.getUser().getUsername();
        this.email = admin.getEmail();
    }

}
