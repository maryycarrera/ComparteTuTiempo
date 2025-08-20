package com.compartetutiempo.timebank.user;

import com.compartetutiempo.timebank.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    String username;

    // TODO: Encriptar. Antes de encriptar, debe tener entre 8 y 12 caracteres alfanuméricos.
    @NotEmpty
    String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    Authority authority;

    public Boolean hasAuthority(Authority auth) {
		return authority.equals(auth);
	}

}
