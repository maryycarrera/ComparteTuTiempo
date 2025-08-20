package com.compartetutiempo.timebank.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compartetutiempo.timebank.exceptions.ResourceNotFoundException;

@Service
public class AdministratorService {

    private final AdministratorRepository administratorRepository;

    @Autowired
    public AdministratorService(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
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

}
