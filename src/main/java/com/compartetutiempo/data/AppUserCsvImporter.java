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

import jakarta.annotation.PostConstruct;

@Component
public class AppUserCsvImporter {

    private final UserRepository userRepository;

    @Autowired
    public AppUserCsvImporter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void importAppUsers() throws IOException {
        List<User> users = CsvImporterUtil.importCsvWithComma("/data/appusers.csv", fields -> {
            User user = new User();
            user.setId(Integer.parseInt(fields[0].trim()));
            user.setUsername(fields[1].trim());
            user.setPassword(fields[2].trim());
            user.setAuthority(Authority.valueOf(fields[3].trim()));
            return user;
        });
        userRepository.saveAll(users);
    }

}
