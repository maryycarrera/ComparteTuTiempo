package com.compartetutiempo.timebank.member;

import java.time.format.DateTimeFormatter;

import lombok.Getter;

@Getter
public class MemberListForMemberDTO {

    private Integer id;
    private String fullName;
    private String username;
    private String dateOfMembership;
    private String timeBalance;

    public MemberListForMemberDTO(Member member) {
        this.id = member.getId();
        this.fullName = member.getFullName();
        this.username = member.getUser().getUsername();

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
