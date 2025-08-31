package com.compartetutiempo.timebank.auth;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compartetutiempo.timebank.admin.AdministratorService;
import com.compartetutiempo.timebank.auth.payload.request.SignupRequest;
import com.compartetutiempo.timebank.member.Member;
import com.compartetutiempo.timebank.member.MemberService;
import com.compartetutiempo.timebank.user.Authority;
import com.compartetutiempo.timebank.user.User;
import com.compartetutiempo.timebank.user.UserService;

import jakarta.validation.Valid;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AdministratorService administratorService;
    private final MemberService memberService;

    @Autowired
    public AuthService(PasswordEncoder passwordEncoder, UserService userService, AdministratorService administratorService, MemberService memberService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.administratorService = administratorService;
        this.memberService = memberService;
    }

    @Transactional
    public void registerMember(@Valid SignupRequest signupRequest) {
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setAuthority(Authority.MEMBER);

        Member member = new Member();
        member.setName(signupRequest.getName());
        member.setLastName(signupRequest.getLastName());
        member.setEmail(signupRequest.getEmail());
        member.setUser(user);

        member.setDateOfMembership(LocalDate.now());
        member.setBiography("");
        member.setHours(5);
        member.setMinutes(0);

        memberService.save(member);
    }

}

