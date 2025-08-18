package com.compartetutiempo.timebank.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    int id;
    String username;
    String name;
    String lastName;
    String email;

}