package com.compartetutiempo.timebank.member.dto;

import com.compartetutiempo.timebank.member.Member;

import lombok.Getter;

@Getter
public class MemberDTO extends BaseMemberDTO {

    private String fullName;

    public MemberDTO(Member member) {
        super(member);
        this.fullName = member.getFullName();
    }

}
