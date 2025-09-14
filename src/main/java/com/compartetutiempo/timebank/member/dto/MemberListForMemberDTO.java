package com.compartetutiempo.timebank.member.dto;

import com.compartetutiempo.timebank.member.Member;

import lombok.Getter;

@Getter
public class MemberListForMemberDTO extends MemberListBaseDTO {

    public MemberListForMemberDTO(Member member) {
        super(member);
    }

}
