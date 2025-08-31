package com.compartetutiempo.timebank.auth;

import java.util.List;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.compartetutiempo.timebank.admin.AdministratorService;
import com.compartetutiempo.timebank.auth.payload.request.LoginRequest;
import com.compartetutiempo.timebank.auth.payload.request.SignupRequest;
import com.compartetutiempo.timebank.auth.payload.response.JwtResponse;
import com.compartetutiempo.timebank.config.jwt.JwtService;
import com.compartetutiempo.timebank.config.userdetails.UserDetailsImpl;
import com.compartetutiempo.timebank.config.userdetails.UserDetailsServiceImpl;
import com.compartetutiempo.timebank.exceptions.AttributeDuplicatedException;
import com.compartetutiempo.timebank.member.MemberService;
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
    public ResponseEntity<Object> logout(@RequestParam(required = false) String token) {
        // START Generado con GitHub Copilot Chat Extension
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (token != null && !token.isEmpty()) {
            jwtBlacklist.add(token, jwtService.getExpiration(token).getTime());
        }
        // END Generado con GitHub Copilot Chat Extension
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().body("Sesión cerrada con éxito.");
    }
    // END Generado con IntelliCode Extension

    @PostMapping("/signup")
    public ResponseEntity<Object> register(@Valid @RequestBody SignupRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            throw new AttributeDuplicatedException("'Nombre de usuario'", request.getUsername());
        }
        if (memberService.findMember(request.getEmail()) != null || administratorService.findAdministrator(request.getEmail()) != null) {
            throw new AttributeDuplicatedException("'Dirección de correo electrónico'", request.getEmail());
        }
        authService.registerMember(request);
        return ResponseEntity.ok().body("Registro exitoso.");
    }

}
