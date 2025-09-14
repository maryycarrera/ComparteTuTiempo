package com.compartetutiempo.timebank.member.dto;

import java.time.format.DateTimeFormatter;

import com.compartetutiempo.timebank.member.Member;

import lombok.Getter;

@Getter
public class MemberDTO {

    private String fullName;
    private String username;
    private String email;
    private String profilePic;
    private String biography;
    private String dateOfMembership;
    private String hours;
    private String minutes;

    public MemberDTO(Member member) {
        this.fullName = member.getFullName();
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
