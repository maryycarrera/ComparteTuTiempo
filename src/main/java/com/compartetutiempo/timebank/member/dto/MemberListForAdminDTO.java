package com.compartetutiempo.timebank.member.dto;

import com.compartetutiempo.timebank.member.Member;

import lombok.Getter;

@Getter
public class MemberListForAdminDTO extends MemberListBaseDTO {

    private String email;

    public MemberListForAdminDTO(Member member) {
        super(member);
        this.email = member.getEmail();
    }
}
