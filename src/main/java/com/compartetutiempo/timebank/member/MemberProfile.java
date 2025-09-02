package com.compartetutiempo.timebank.member;

import lombok.Getter;

@Getter
public class MemberProfile {

    private String name;
    private String lastName;
    private String username;
    private String email;
    private String profilePic;
    private String biography;
    private String dateOfMembership;
    private String hours;
    private String minutes;

    public MemberProfile(Member member) {
        this.name = member.getName();
        this.lastName = member.getLastName();
        this.username = member.getUser().getUsername();
        this.email = member.getEmail();
        this.profilePic = member.getProfilePicture();
        this.biography = member.getBiography();
        this.dateOfMembership = member.getDateOfMembership().toString();
        this.hours = member.getHours().toString();
        this.minutes = member.getMinutes().toString();
    }

}
