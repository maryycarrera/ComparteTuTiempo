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
import com.compartetutiempo.timebank.auth.payload.request.SignupRequest;
import com.compartetutiempo.timebank.exceptions.ResourceNotFoundException;
import com.compartetutiempo.timebank.member.Member;
import com.compartetutiempo.timebank.member.MemberRepository;
import com.compartetutiempo.timebank.user.Authority;
import com.compartetutiempo.timebank.user.User;
import com.compartetutiempo.timebank.user.UserRepository;
import jakarta.validation.Valid;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AdministratorRepository administratorRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository, AdministratorRepository administratorRepository, MemberRepository memberRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.administratorRepository = administratorRepository;
        this.memberRepository = memberRepository;
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

        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public String getFullName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            throw new AuthenticationCredentialsNotFoundException("No hay usuario autenticado actualmente.");
        }

        String username = auth.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        if (user.getAuthority() == Authority.ADMIN) {
            Administrator admin = administratorRepository.findAdministratorByUser(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Administrator", "username", username));
            return admin.getFullName();
        } else if (user.getAuthority() == Authority.MEMBER) {
            Member member = memberRepository.findMemberByUser(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Member", "username", username));
            return member.getFullName();
        } else {
            throw new AuthenticationCredentialsNotFoundException("Unknown user authority.");
        }
    }

}

