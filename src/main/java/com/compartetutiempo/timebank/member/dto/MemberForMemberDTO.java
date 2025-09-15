package com.compartetutiempo.timebank.member.dto;

import com.compartetutiempo.timebank.member.Member;

import lombok.Getter;

@Getter
public class MemberForMemberDTO extends BaseMemberDTO {

    private String fullName;

    public MemberForMemberDTO(Member member) {
        super(member);
        this.fullName = member.getFullName();
    }

}
