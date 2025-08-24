package com.compartetutiempo.timebank.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.compartetutiempo.timebank.admin.AdministratorService;
import com.compartetutiempo.timebank.member.MemberService;
import com.compartetutiempo.timebank.user.UserService;

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

    // public AuthResponse login(LoginRequest request) {
    //     authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    //     User user = userRepository.findByUsername(request.getUsername())
    //             .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + request.getUsername()));
    //     String token = jwtService.getToken(user);
    //     return AuthResponse.builder()
    //             .token(token)
    //             .build();
    // }

//     public AuthResponse register(RegisterRequest request) {
//         User user = User.builder()
//                 .name(request.getName())
//                 .lastName(request.getLastName())
//                 .username(request.getUsername())
//                 .password(passwordEncoder.encode(request.getPassword()))
//                 .email(request.getEmail())
//                 .role(Authority.MEMBER)
//                 .build();

//         userRepository.save(user);

//         return AuthResponse.builder()
//                 .token(jwtService.getToken(user))
//                 .build();
//     }
}
