/*
 * Clase creada con GitHub Copilot Chat Extension.
 */

package com.compartetutiempo.data;

import com.compartetutiempo.timebank.admin.Administrator;
import com.compartetutiempo.timebank.admin.AdministratorRepository;
import com.compartetutiempo.timebank.user.UserRepository;
import com.compartetutiempo.timebank.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Component
public class AdministratorCsvImporter {

    private final AdministratorRepository administratorRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdministratorCsvImporter(AdministratorRepository administratorRepository, UserRepository userRepository) {
        this.administratorRepository = administratorRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void importAdministrators() throws IOException {
        List<Administrator> admins = CsvImporterUtil.importCsvWithComma("/data/admins.csv", fields -> {
            Administrator admin = new Administrator();
            admin.setName(fields[0].replaceAll("'", "").trim());
            admin.setLastName(fields[1].replaceAll("'", "").trim());
            admin.setEmail(fields[2].trim());
            admin.setProfilePicture(fields[3].trim());
            int userId = Integer.parseInt(fields[4].trim());
            User user = userRepository.findById(userId).orElse(null);
            admin.setUser(user);
            return admin;
        });
        administratorRepository.saveAll(admins);
    }
}
