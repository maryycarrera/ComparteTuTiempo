package com.compartetutiempo.timebank.admin;

import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compartetutiempo.timebank.admin.dto.AdminDTO;
import com.compartetutiempo.timebank.admin.dto.AdminEditDTO;
import com.compartetutiempo.timebank.admin.dto.AdminForListDTO;
import com.compartetutiempo.timebank.exceptions.InvalidProfilePictureException;
import com.compartetutiempo.timebank.exceptions.ResourceNotFoundException;
import com.compartetutiempo.timebank.payload.request.SignupRequest;
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
    public List<AdminForListDTO> findAllForList() {
        return StreamSupport.stream(administratorRepository.findAll().spliterator(), false)
                .sorted(Comparator.comparing(Administrator::getFullName, String.CASE_INSENSITIVE_ORDER))
                .map(AdminForListDTO::new)
                .toList();
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
    public AdminDTO findAdministratorDTO(Integer adminId) {
        return administratorRepository.findById(adminId)
                .map(AdminDTO::new)
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
        admin.setProfilePicture("profilepics/black.png");

        return administratorRepository.save(admin);
    }

    @Transactional
    public void delete(Integer adminId) {
        if(!administratorRepository.existsById(adminId)) {
            throw new ResourceNotFoundException("Administrator", "id", adminId);
        }
        administratorRepository.deleteById(adminId);
    }

    @Transactional
    public AdminDTO updateByUsername(String username, @Valid AdminEditDTO adminDTO) {
        Administrator admin = findAdministratorByUser(username);

        admin.setName(adminDTO.getName());
        admin.setLastName(adminDTO.getLastName());

        return new AdminDTO(administratorRepository.save(admin));
    }

    @Transactional
    public AdminDTO updateProfilePicture(String username, String color) {
        color = color.toLowerCase().trim();

        if (color == null || color.isBlank()) {
            throw new InvalidProfilePictureException("El color de la imagen de perfil no puede estar vacío.");
        }

        List<String> validColors = List.of("blue", "gray", "green", "orange", "pink", "purple", "red", "yellow");
        if (color.equals("black") || !validColors.contains(color)) {
            throw new InvalidProfilePictureException(color, "updateProfilePicture");
        }

        String profilePicture = "profilepics/" + color + ".png";
        Administrator admin = findAdministratorByUser(username);
        admin.setProfilePicture(profilePicture);
        return new AdminDTO(administratorRepository.save(admin));
    }

}
