package com.compartetutiempo.data;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.compartetutiempo.timebank.exceptions.ResourceNotFoundException;
import com.compartetutiempo.timebank.member.Member;
import com.compartetutiempo.timebank.member.MemberRepository;
import com.compartetutiempo.timebank.user.User;
import com.compartetutiempo.timebank.user.UserRepository;
import com.opencsv.exceptions.CsvValidationException;

import jakarta.annotation.PostConstruct;

@Component
@DependsOn("appUserCsvImporter")
public class MemberCsvImporter {

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    @Autowired
    public MemberCsvImporter(MemberRepository memberRepository, UserRepository userRepository) {
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
        System.out.println("[IMPORT] Bean MemberCsvImporter creado");
    }

    @PostConstruct
    public void importMembers() throws CsvValidationException, IOException {
        System.out.println("[IMPORT] Iniciando importación de miembros...");
        try {
            List<Member> members = CsvImporterUtil.importCsvWithComma("/data/members.csv", fields -> {
                Member member = new Member();
                member.setName(fields[0].trim());
                member.setLastName(fields[1].trim());
                member.setEmail(fields[2].trim());

                String profilePictureStr = fields[3].trim();
                member.setProfilePicture(profilePictureStr.isEmpty() ? null : profilePictureStr);

                String dateStr = fields[4].trim();
                member.setDateOfMembership(LocalDate.parse(dateStr));

                String biographyStr = fields[5].trim();
                member.setBiography(biographyStr.isEmpty() ? null : biographyStr);

                member.setHours(Integer.parseInt(fields[6].trim()));
                member.setMinutes(Integer.parseInt(fields[7].trim()));
                String username = fields[8].trim();
                User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
                member.setUser(user);
                return member;
            });
            memberRepository.saveAll(members);
            System.out.println("[IMPORT] Miembros importados: " + members.size());
        } catch (Exception e) {
            System.out.println("[IMPORT][ERROR] Error importando miembros: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
