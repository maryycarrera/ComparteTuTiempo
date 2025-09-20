package com.compartetutiempo.timebank.member.dto;

import com.compartetutiempo.timebank.model.dto.PersonDTO;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MemberEditDTO extends PersonDTO {

    @Size(max = 500)
    private String biography;

}
