package com.compartetutiempo.timebank.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Getter;

@Getter
public class PersonDTO {

    @NotEmpty
    @Size(min = 3, max = 100)
    private String name;

    @NotEmpty
    @Size(min = 3, max = 100)
    private String lastName;

}
