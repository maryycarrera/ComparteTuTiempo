/*
 * Clase creada con GitHub Copilot Chat Extension.
 */

package com.compartetutiempo.data;

import com.compartetutiempo.timebank.admin.Administrator;
import com.compartetutiempo.timebank.admin.AdministratorRepository;
import com.opencsv.exceptions.CsvValidationException;
import com.compartetutiempo.timebank.user.Authority;
import com.compartetutiempo.timebank.user.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.core.env.Environment;
import com.compartetutiempo.util.ProfileUtils;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Component
public class AdministratorCsvImporter {

    private final AdministratorRepository administratorRepository;
    private final Environment env;

    private static final Logger logger = LoggerFactory.getLogger(AdministratorCsvImporter.class);

    @Autowired
    public AdministratorCsvImporter(AdministratorRepository administratorRepository, Environment env) {
        this.administratorRepository = administratorRepository;
        this.env = env;
        logger.info("[IMPORT] Bean AdministratorCsvImporter creado");
    }

    @PostConstruct
    public void importAdministrators() throws IOException, CsvValidationException {
        logger.info("[IMPORT] Iniciando importación de administradores...");
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
            if (ProfileUtils.isProd(env)) {
                int importados = 0;
                for (Administrator admin : admins) {
                    try {
                        administratorRepository.save(admin);
                        importados++;
                    } catch (Exception e) {
                        logger.warn("[IMPORT][WARN] Administrador duplicado ignorado: " + admin.getUser().getUsername());
                    }
                }
                logger.info("[IMPORT] Administradores importados: " + importados);
            } else {
                administratorRepository.saveAll(admins);
                logger.info("[IMPORT] Administradores importados: " + admins.size());
            }
        } catch (Exception e) {
            logger.error("[IMPORT][ERROR] Error importando administradores: " + e.getMessage());
            if(!ProfileUtils.isProd(env)) {
                logger.debug("Stack Trace: ", e);
            }
        }
    }
}
