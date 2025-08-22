package com.compartetutiempo.data;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compartetutiempo.timebank.exceptions.ResourceNotFoundException;
import com.compartetutiempo.timebank.member.Member;
import com.compartetutiempo.timebank.member.MemberRepository;
import com.compartetutiempo.timebank.user.User;
import com.compartetutiempo.timebank.user.UserRepository;
import com.opencsv.exceptions.CsvValidationException;

import jakarta.annotation.PostConstruct;

@Component
public class MemberCsvImporter {

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    @Autowired
    public MemberCsvImporter(MemberRepository memberRepository, UserRepository userRepository) {
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void importMembers() throws CsvValidationException, IOException {
        List<Member> members = CsvImporterUtil.importCsvWithComma("/data/members.csv", fields -> {
            Member member = new Member();
            member.setId(Integer.parseInt(fields[0].trim()));
            member.setName(fields[1].trim());
            member.setLastName(fields[2].trim());
            member.setEmail(fields[3].trim());

            String profilePictureStr = fields[4].trim();
            member.setProfilePicture(profilePictureStr.isEmpty() ? null : profilePictureStr);

            String dateStr = fields[5].trim();
            member.setDateOfMembership(LocalDate.parse(dateStr));

            String biographyStr = fields[6].trim();
            member.setBiography(biographyStr.isEmpty() ? null : biographyStr);

            member.setHours(Integer.parseInt(fields[7].trim()));
            member.setMinutes(Integer.parseInt(fields[8].trim()));
            int userId = Integer.parseInt(fields[9].trim());
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
            member.setUser(user);
            return member;
        });
        memberRepository.saveAll(members);
    }
}
