package com.compartetutiempo.data;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.core.env.Environment;
import com.compartetutiempo.util.ProfileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compartetutiempo.timebank.member.Member;
import com.compartetutiempo.timebank.member.MemberRepository;
import com.compartetutiempo.timebank.user.Authority;
import com.compartetutiempo.timebank.user.User;
import com.opencsv.exceptions.CsvValidationException;

import jakarta.annotation.PostConstruct;

@Component
public class MemberCsvImporter {

    private final MemberRepository memberRepository;
    private final Environment env;

    private static final Logger logger = LoggerFactory.getLogger(MemberCsvImporter.class);

    @Autowired
    public MemberCsvImporter(MemberRepository memberRepository, Environment env) {
        this.memberRepository = memberRepository;
        this.env = env;
        logger.info("[IMPORT] Bean MemberCsvImporter creado");
    }

    @PostConstruct
    public void importMembers() throws CsvValidationException, IOException {
        logger.info("[IMPORT] Iniciando importación de miembros...");
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

                User user = new User();
                user.setUsername(fields[8].trim());
                user.setPassword(fields[9].trim());
                user.setAuthority(Authority.MEMBER);
                member.setUser(user);

                return member;
            });
            if (ProfileUtils.isProd(env)) {
                int importados = 0;
                for (Member member : members) {
                    try {
                        memberRepository.save(member);
                        importados++;
                    } catch (Exception e) {
                        logger.warn("[IMPORT][WARN] Miembro duplicado ignorado: " + member.getUser().getUsername());
                    }
                }
                logger.info("[IMPORT] Miembros importados: " + importados);
            } else {
                memberRepository.saveAll(members);
                logger.info("[IMPORT] Miembros importados: " + members.size());
            }
        } catch (Exception e) {
            logger.error("[IMPORT][ERROR] Error importando miembros: " + e.getMessage());
            if(!ProfileUtils.isProd(env)) {
                logger.debug("Stack Trace: ", e);
            }
        }
    }
}
