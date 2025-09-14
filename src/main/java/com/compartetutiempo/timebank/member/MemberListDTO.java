package com.compartetutiempo.timebank.member;

import java.time.format.DateTimeFormatter;

import lombok.Getter;

@Getter
public class MemberListDTO {

    private String fullName;
    private String username;
    private String email;
    private String dateOfMembership;
    private String timeBalance;

    public MemberListDTO(Member member) {
        this.fullName = member.getFullName();
        this.username = member.getUser().getUsername();
        this.email = member.getEmail();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.dateOfMembership = member.getDateOfMembership().format(formatter);

        this.timeBalance = member.getTimeBalance().toString();
    }

}
