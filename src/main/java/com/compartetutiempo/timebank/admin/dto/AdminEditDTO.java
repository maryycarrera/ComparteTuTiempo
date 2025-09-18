package com.compartetutiempo.timebank.admin.dto;

import com.compartetutiempo.timebank.admin.Administrator;
import com.compartetutiempo.timebank.model.dto.PersonDTO;

public class AdminEditDTO extends PersonDTO {

    public AdminEditDTO(Administrator admin) {
        super(admin);
    }

}
