package com.compartetutiempo.timebank.auth.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    @NotEmpty
    @Size(min = 3, max = 100)
    private String name;

    @NotEmpty
    @Size(min = 3, max = 100)
    private String lastName;

    @NotEmpty
    @Size(min = 5, max = 15)
    @Pattern(regexp = "^[a-z0-9_.]+$", message = "El nombre de usuario solo puede contener letras minúsculas (sin tildes, excluyendo 'ñ' y 'ç'), números, guiones bajos y puntos.")
    private String username;

    @NotEmpty
    @Size(max = 255)
    @Email
    private String email;

    @NotEmpty
    @Size(min = 8, max = 12)
    private String password;

}
