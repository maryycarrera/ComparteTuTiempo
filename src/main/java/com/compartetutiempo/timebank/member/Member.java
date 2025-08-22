package com.compartetutiempo.timebank.member;

import java.time.LocalDate;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import com.compartetutiempo.timebank.model.Person;
import com.compartetutiempo.timebank.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "members")
public class Member extends Person {

    @Column(name = "date_of_membership")
    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @PastOrPresent
    private LocalDate dateOfMembership;

    @Size(max = 500)
    private String biography;

    @NotNull
    @Min(-5)
    @Max(10)
    private Integer hours;

    @NotNull
    @Min(-59) // TODO: Puede ser negativo si hours es negativo o 0. Si hours es positivo, debe ser positivo.
    @Max(59)
    private Integer minutes;

    @OneToOne(cascade = { CascadeType.DETACH, CascadeType.REFRESH, CascadeType.PERSIST })
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    // TODO: Debe estar entre -5 y 10
    public Double getTimeBalance() {
        return hours + (double) minutes / 60.0;
    }

}
