package com.compartetutiempo.timebank.user;

import com.compartetutiempo.timebank.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "appusers")
public class User extends BaseEntity {

    @Column(name = "username", unique = true)
    @NotEmpty
    @Size(min = 5, max = 15)
    @Pattern(regexp = "^[a-z0-9_.]+$", message = "El nombre de usuario solo puede contener letras minúsculas (sin tildes, excluyendo 'ñ' y 'ç'), números, guiones bajos y puntos.")
    String username;

    @NotEmpty
    String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    Authority authority;

    public Boolean hasAuthority(Authority auth) {
		return authority.equals(auth);
	}

}
