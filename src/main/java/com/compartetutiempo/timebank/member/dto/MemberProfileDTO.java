package com.compartetutiempo.timebank.member.dto;

import lombok.Getter;
import com.compartetutiempo.timebank.member.Member;

@Getter
public class MemberProfileDTO extends BaseMemberDTO {

    private String name;
    private String lastName;
    private String email;

    public MemberProfileDTO(Member member) {
        super(member);
        this.name = member.getName();
        this.lastName = member.getLastName();
        this.email = member.getEmail();
    }

}
