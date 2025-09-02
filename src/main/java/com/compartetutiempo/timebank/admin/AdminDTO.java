package com.compartetutiempo.timebank.admin;

import lombok.Getter;

@Getter
public class AdminDTO {

    private String name;
    private String lastName;
    private String username;
    private String email;
    private String profilePic;

    public AdminDTO(Administrator admin) {
        this.name = admin.getName();
        this.lastName = admin.getLastName();
        this.username = admin.getUser().getUsername();
        this.email = admin.getEmail();
        this.profilePic = admin.getProfilePicture();
    }

}
