package com.compartetutiempo.timebank.member.dto;

import java.time.format.DateTimeFormatter;

import com.compartetutiempo.timebank.member.Member;

import lombok.Getter;

@Getter
public class MemberListForAdminDTO {

    private Integer id;
    private String fullName;
    private String username;
    private String email;
    private String dateOfMembership;
    private String timeBalance;

    public MemberListForAdminDTO(Member member) {
        this.id = member.getId();
        this.fullName = member.getFullName();
        this.username = member.getUser().getUsername();
        this.email = member.getEmail();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.dateOfMembership = member.getDateOfMembership().format(formatter);

        int hours = member.getHours();
        int minutes = member.getMinutes();

        if (hours != 0) {
            this.timeBalance = new StringBuilder()
                .append(hours).append("h ")
                .append(Math.abs(minutes)).append("min")
                .toString();
        } else {
            this.timeBalance = new StringBuilder()
                .append(minutes < 0 ? "-" : "")
                .append("0h ")
                .append(Math.abs(minutes)).append("min")
                .toString();
        }
    }

}
