package com.compartetutiempo.timebank.solidarityfund;

import com.compartetutiempo.timebank.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "solidarity_fund")
public class SolidarityFund extends BaseEntity {

    @NotNull
    @Min(0)
    private Integer hours;

    @NotNull
    @Min(0)
    @Max(59)
    private Integer minutes;

    public Double getTimeBalance() {
        return hours + (double) minutes / 60.0;
    }

}
