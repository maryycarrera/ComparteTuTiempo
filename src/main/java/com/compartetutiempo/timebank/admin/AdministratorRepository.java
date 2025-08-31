package com.compartetutiempo.timebank.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AdministratorRepository extends CrudRepository<Administrator, Integer> {

    @Query("SELECT admin FROM Administrator admin WHERE admin.user.username = :username")
    Optional<Administrator> findAdministratorByUser(String username);

    @Query("SELECT admin FROM Administrator admin WHERE admin.user.id = :userId")
    Optional<Administrator> findAdministratorByUser(int userId);

    Optional<Administrator> findByEmail(String email);

    Boolean existsByEmail(String email);

}
