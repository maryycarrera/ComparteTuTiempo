package com.compartetutiempo.timebank.auth;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.compartetutiempo.timebank.admin.Administrator;
import com.compartetutiempo.timebank.admin.AdministratorService;
import com.compartetutiempo.timebank.admin.dto.AdminDTO;
import com.compartetutiempo.timebank.auth.payload.request.LoginRequest;
import com.compartetutiempo.timebank.auth.payload.response.JwtResponse;
import com.compartetutiempo.timebank.config.jwt.JwtService;
import com.compartetutiempo.timebank.config.userdetails.UserDetailsImpl;
import com.compartetutiempo.timebank.config.userdetails.UserDetailsServiceImpl;
import com.compartetutiempo.timebank.exceptions.AttributeDuplicatedException;
import com.compartetutiempo.timebank.member.Member;
import com.compartetutiempo.timebank.member.MemberService;
import com.compartetutiempo.timebank.member.dto.MemberProfileDTO;
import com.compartetutiempo.timebank.payload.request.SignupRequest;
import com.compartetutiempo.timebank.payload.response.MessageResponse;
import com.compartetutiempo.timebank.user.Authority;
import com.compartetutiempo.timebank.user.User;
import com.compartetutiempo.timebank.user.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthService authService;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtBlacklist jwtBlacklist;
    private final MemberService memberService;
    private final AdministratorService administratorService;

    @Autowired
    public AuthRestController(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService, AuthService authService, UserDetailsServiceImpl userDetailsService, JwtBlacklist jwtBlacklist, MemberService memberService, AdministratorService administratorService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
        this.authService = authService;
        this.userDetailsService = userDetailsService;
        this.jwtBlacklist = jwtBlacklist;
        this.memberService = memberService;
        this.administratorService = administratorService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwtToken = jwtService.generateToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

            return ResponseEntity.ok().body(new JwtResponse(jwtToken, userDetails.getUsername(), userDetails.getId(), roles));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Credenciales no válidas.");
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<Object> validateToken(@RequestParam String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String username = jwtService.getUsernameFromToken(token);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido.");
        }

        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(username);
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        if (isValid) {
            return ResponseEntity.ok().body("Token válido.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no válido.");
        }
    }

    // START Generado con IntelliCode Extension
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse<String>> logout(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (token != null && !token.isEmpty()) {
            jwtBlacklist.add(token, jwtService.getExpiration(token).getTime());
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().body(new MessageResponse<String>("Sesión cerrada con éxito."));
    }
    // END Generado con IntelliCode Extension

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse<Member>> register(@Valid @RequestBody SignupRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            throw new AttributeDuplicatedException("Nombre de usuario", request.getUsername());
        }
        if (memberService.existsByEmail(request.getEmail()) || administratorService.existsByEmail(request.getEmail())) {
            throw new AttributeDuplicatedException("Dirección de correo electrónico", request.getEmail());
        }
        Member member = authService.registerMember(request);
        return ResponseEntity.ok().body(new MessageResponse<Member>("Registro exitoso.", member));
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> getProfile() {
        User currentUser = userService.findCurrentUser();

        Authority authority = currentUser.getAuthority();
        if (authority.equals(Authority.ADMIN)) {
            Administrator admin = administratorService.findAdministratorByUser(currentUser.getId());
            AdminDTO adminDTO = new AdminDTO(admin);
            return ResponseEntity.ok().body(adminDTO);
        } else if (authority.equals(Authority.MEMBER)) {
            Member member = memberService.findMemberByUser(currentUser.getId());
            MemberProfileDTO memberProfile = new MemberProfileDTO(member);
            return ResponseEntity.ok().body(memberProfile);
        } else {
            throw new IllegalStateException("El usuario tiene un rol desconocido.");
        }
    }

    @GetMapping("/fullname")
    public ResponseEntity<MessageResponse<String>> getFullName() {
        String fullName = authService.getFullName();
        return ResponseEntity.ok().body(new MessageResponse<String>("Nombre completo recuperado con éxito.", fullName));
    }

    @GetMapping("/person-id-is-me/{personId}")
    public ResponseEntity<MessageResponse<Boolean>> isCurrentUserPersonId(@PathVariable("personId") Integer personId) {
        Boolean isMe = authService.isCurrentUserPersonId(personId);
        return ResponseEntity.ok().body(new MessageResponse<Boolean>("Verificación de identidad realizada con éxito.", isMe));
    }

}
