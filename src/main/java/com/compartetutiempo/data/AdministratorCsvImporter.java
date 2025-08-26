/*
 * Clase creada con GitHub Copilot Chat Extension.
 */

package com.compartetutiempo.data;

import com.compartetutiempo.timebank.admin.Administrator;
import com.compartetutiempo.timebank.admin.AdministratorRepository;
import com.opencsv.exceptions.CsvValidationException;
import com.compartetutiempo.timebank.user.Authority;
import com.compartetutiempo.timebank.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Component
public class AdministratorCsvImporter {

    private final AdministratorRepository administratorRepository;

    @Autowired
    public AdministratorCsvImporter(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
        System.out.println("[IMPORT] Bean AdministratorCsvImporter creado");
    }

    @PostConstruct
    public void importAdministrators() throws IOException, CsvValidationException {
        System.out.println("[IMPORT] Iniciando importación de administradores...");
        try {
            List<Administrator> admins = CsvImporterUtil.importCsvWithComma("/data/admins.csv", fields -> {
                Administrator admin = new Administrator();
                admin.setName(fields[0].trim());
                admin.setLastName(fields[1].trim());
                admin.setEmail(fields[2].trim());

                String profilePictureStr = fields[3].trim();
                admin.setProfilePicture(profilePictureStr.isEmpty() ? null : profilePictureStr);

                User user = new User();
                user.setUsername(fields[4].trim());
                user.setPassword(fields[5].trim());
                user.setAuthority(Authority.ADMIN);
                admin.setUser(user);

                return admin;
            });
            administratorRepository.saveAll(admins);
            System.out.println("[IMPORT] Administradores importados: " + admins.size());
        } catch (Exception e) {
            System.out.println("[IMPORT][ERROR] Error importando administradores: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
