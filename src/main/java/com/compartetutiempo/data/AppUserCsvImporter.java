/*
 * Clase creada con GitHub Copilot Chat Extension.
 */

package com.compartetutiempo.data;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compartetutiempo.timebank.user.User;
import com.compartetutiempo.timebank.user.Authority;
import com.compartetutiempo.timebank.user.UserRepository;
import com.opencsv.exceptions.CsvValidationException;

import jakarta.annotation.PostConstruct;

@Component
public class AppUserCsvImporter {

    private final UserRepository userRepository;

    @Autowired
    public AppUserCsvImporter(UserRepository userRepository) {
    this.userRepository = userRepository;
    System.out.println("[IMPORT] Bean AppUserCsvImporter creado");
    }

    @PostConstruct
    public void importAppUsers() throws IOException, CsvValidationException {
        System.out.println("[IMPORT] Iniciando importación de usuarios...");
        try {
            List<User> users = CsvImporterUtil.importCsvWithComma("/data/appusers.csv", fields -> {
                User user = new User();
                user.setUsername(fields[0].trim());
                user.setPassword(fields[1].trim());
                user.setAuthority(Authority.valueOf(fields[2].trim()));
                return user;
            });
            userRepository.saveAll(users);
            System.out.println("[IMPORT] Usuarios importados: " + users.size());
        } catch (Exception e) {
            System.out.println("[IMPORT][ERROR] Error importando usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
