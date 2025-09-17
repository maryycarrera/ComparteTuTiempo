package com.compartetutiempo.timebank.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compartetutiempo.timebank.admin.dto.AdminForListDTO;
import com.compartetutiempo.timebank.exceptions.AttributeDuplicatedException;
import com.compartetutiempo.timebank.member.MemberService;
import com.compartetutiempo.timebank.payload.request.SignupRequest;
import com.compartetutiempo.timebank.payload.response.ListMessageResponse;
import com.compartetutiempo.timebank.payload.response.MessageResponse;
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
    public ResponseEntity<ListMessageResponse<AdminForListDTO>> findAllAdministrators() {
        List<AdminForListDTO> adminDTOs = administratorService.findAllForList();

        if (adminDTOs.isEmpty()) {
            ListMessageResponse<AdminForListDTO> response = new ListMessageResponse<AdminForListDTO>("¡Vaya! Parece que no hay administradores registrados aún.");
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.ok(new ListMessageResponse<AdminForListDTO>("Lista de administradores encontrada con éxito.", adminDTOs));
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

    @DeleteMapping(value = "{adminId}")
    public ResponseEntity<String> delete(@PathVariable("adminId") Integer adminId) {
        administratorService.delete(adminId);
        return ResponseEntity.ok("Administrador con ID " + adminId + " eliminado con éxito.");
    }

}
