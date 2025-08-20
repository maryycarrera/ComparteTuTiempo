package com.compartetutiempo.timebank.admin;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admins")
public class AdministratorRestController {

    private final AdministratorService administratorService;

    @Autowired
    public AdministratorRestController(AdministratorService administratorService) {
        this.administratorService = administratorService;
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

}
