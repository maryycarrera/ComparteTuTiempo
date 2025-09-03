package com.compartetutiempo.timebank.auth;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compartetutiempo.timebank.admin.Administrator;
import com.compartetutiempo.timebank.admin.AdministratorRepository;
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
    private final AdministratorRepository administratorRepository;
    private final MemberService memberService;

    @Autowired
    public AuthService(PasswordEncoder passwordEncoder, UserService userService, AdministratorService administratorService, MemberService memberService, AdministratorRepository administratorRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.administratorService = administratorService;
        this.memberService = memberService;
        this.administratorRepository = administratorRepository;
    }

    @Transactional
    public Member registerMember(@Valid SignupRequest signupRequest) {
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
        member.setProfilePicture("/profilepics/gray.png");

        return memberService.save(member);
    }

    @Transactional(readOnly = true)
    public String getFullName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("No hay usuario autenticado actualmente.");
        }

        String username = auth.getName();

        Administrator admin = administratorRepository.findAdministratorByUser(username).orElse(null);
        if (admin != null) {
            return admin.getFullName();
        }

        Member member = memberService.findMemberByUser(username);
        return member.getFullName();
    }

}

