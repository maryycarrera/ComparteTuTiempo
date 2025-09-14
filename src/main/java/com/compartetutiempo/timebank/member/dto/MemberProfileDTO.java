package com.compartetutiempo.timebank.member.dto;

import lombok.Getter;
import java.time.format.DateTimeFormatter;

import com.compartetutiempo.timebank.member.Member;

@Getter
public class MemberProfileDTO {

    private String name;
    private String lastName;
    private String username;
    private String email;
    private String profilePic;
    private String biography;
    private String dateOfMembership;
    private String hours;
    private String minutes;

    public MemberProfileDTO(Member member) {
        this.name = member.getName();
        this.lastName = member.getLastName();
        this.username = member.getUser().getUsername();
        this.email = member.getEmail();
        this.profilePic = member.getProfilePicture();
        this.biography = member.getBiography();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.dateOfMembership = member.getDateOfMembership().format(formatter);

        this.hours = member.getHours().toString();
        this.minutes = member.getMinutes().toString();
    }

}
