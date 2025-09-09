package com.compartetutiempo.timebank.solidarityfund;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SolidarityFundRepository extends CrudRepository<SolidarityFund, Integer> {

    @Query("SELECT sf FROM SolidarityFund sf WHERE sf.id = 1")
    Optional<SolidarityFund> find();

}
