package com.compartetutiempo.timebank.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    @Modifying()
    @Query("UPDATE User u SET u.name = :name, u.lastName = :lastName WHERE u.id = :id")
    void updateUser(
        @Param(value = "id") Integer id,
        @Param(value = "name") String name,
        @Param(value = "lastName") String lastName
    );

}
