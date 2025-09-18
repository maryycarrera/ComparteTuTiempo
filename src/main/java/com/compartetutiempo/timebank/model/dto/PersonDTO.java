package com.compartetutiempo.timebank.model.dto;

import com.compartetutiempo.timebank.model.Person;

import lombok.Getter;

@Getter
public class PersonDTO {

    private String name;
    private String lastName;

    public PersonDTO(Person person) {
        this.name = person.getName();
        this.lastName = person.getLastName();
    }

}
