package com.compartetutiempo.timebank.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Integer> {

    @Query("SELECT m FROM Member m WHERE m.user.username = :username")
    Optional<Member> findMemberByUser(String username);

    @Query("SELECT m FROM Member m WHERE m.user.id = :userId")
    Optional<Member> findMemberByUser(int userId);

    @Query("SELECT m FROM Member m WHERE m.email = :email")
    Optional<Member> findMemberByEmail(String email);

}
