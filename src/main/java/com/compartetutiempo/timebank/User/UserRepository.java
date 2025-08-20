package com.compartetutiempo.timebank.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    @NonNull Optional<User> findById(@NonNull Integer id);

    Boolean existsByUsername(String username);

    // @Query("SELECT admin FROM Administrator admin WHERE admin.user.username = :username")
    // Optional<Administrator> findAdministratorByUser(String username);

    // @Query("SELECT admin FROM Administrator admin WHERE admin.user.id = :id")
    // Optional<Administrator> findAdministratorByUser(int id);

    // @Query("SELECT m FROM Member m WHERE m.user.username = :username")
    // Optional<Member> findMemberByUser(String username);

    // @Query("SELECT m FROM Member m WHERE m.user.id = :id")
    // Optional<Member> findMemberByUser(int id);

    @Query("SELECT u FROM User u WHERE u.authority = :authority")
    Iterable<User> findAllByAuthority(Authority authority);

}
