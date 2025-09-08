package com.compartetutiempo.timebank.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compartetutiempo.timebank.auth.payload.request.SignupRequest;
import com.compartetutiempo.timebank.exceptions.ResourceNotFoundException;
import com.compartetutiempo.timebank.user.Authority;
import com.compartetutiempo.timebank.user.User;

import jakarta.validation.Valid;

@Service
public class AdministratorService {

    private final AdministratorRepository administratorRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdministratorService(AdministratorRepository administratorRepository, PasswordEncoder passwordEncoder) {
        this.administratorRepository = administratorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Iterable<Administrator> findAll() {
        return administratorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Administrator findAdministratorByUser(String username) {
        return administratorRepository.findAdministratorByUser(username)
                .orElseThrow(() -> new ResourceNotFoundException("Administrator", "username", username));
    }

    @Transactional(readOnly = true)
    public Administrator findAdministratorByUser(int userId) {
        return administratorRepository.findAdministratorByUser(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Administrator", "id", userId));
    }

    @Transactional(readOnly = true)
    public Administrator findAdministrator(Integer adminId) {
        return administratorRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Administrator", "id", adminId));
    }

    @Transactional(readOnly = true)
    public Administrator findAdministrator(String email) {
        return administratorRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Administrator", "email", email));
    }

    public Boolean existsByEmail(String email) {
        return administratorRepository.existsByEmail(email);
    }

    @Transactional
    public Administrator create(@Valid SignupRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAuthority(Authority.ADMIN);

        Administrator admin = new Administrator();
        admin.setName(request.getName());
        admin.setLastName(request.getLastName());
        admin.setEmail(request.getEmail());

        admin.setUser(user);
        admin.setProfilePicture("/profilepics/gray.png");

        return administratorRepository.save(admin);
    }

}
