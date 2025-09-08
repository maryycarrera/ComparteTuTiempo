package com.compartetutiempo.timebank.admin;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compartetutiempo.timebank.auth.payload.request.SignupRequest;
import com.compartetutiempo.timebank.exceptions.AttributeDuplicatedException;
import com.compartetutiempo.timebank.member.MemberService;
import com.compartetutiempo.timebank.auth.payload.response.MessageResponse;
import com.compartetutiempo.timebank.user.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admins")
public class AdministratorRestController {

    private final AdministratorService administratorService;
    private final UserService userService;
    private final MemberService memberService;

    @Autowired
    public AdministratorRestController(AdministratorService administratorService, UserService userService, MemberService memberService) {
        this.administratorService = administratorService;
        this.userService = userService;
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<Administrator>> findAllAdministrators() {
        List<Administrator> administrators = StreamSupport
                .stream(administratorService.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return ResponseEntity.ok(administrators);
    }

    @GetMapping(value = "{adminId}")
    public ResponseEntity<Administrator> findById(@PathVariable("adminId") Integer adminId) {
        Administrator administrator = administratorService.findAdministrator(adminId);
        return ResponseEntity.ok(administrator);
    }

    @PostMapping()
    public ResponseEntity<MessageResponse<Administrator>> createAdministrator(@Valid @RequestBody SignupRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            throw new AttributeDuplicatedException("Nombre de usuario", request.getUsername());
        }
        if (memberService.existsByEmail(request.getEmail()) || administratorService.existsByEmail(request.getEmail())) {
            throw new AttributeDuplicatedException("Dirección de correo electrónico", request.getEmail());
        }
        Administrator createdAdmin = administratorService.create(request);
        return ResponseEntity.ok().body(new MessageResponse<Administrator>("Registro exitoso.", createdAdmin));
    }

}
