package com.compartetutiempo.timebank.member;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compartetutiempo.timebank.exceptions.ResourceNotFoundException;
import com.compartetutiempo.timebank.member.dto.MemberDTO;
import com.compartetutiempo.timebank.member.dto.MemberForMemberDTO;
import com.compartetutiempo.timebank.member.dto.MemberListForAdminDTO;
import com.compartetutiempo.timebank.member.dto.MemberListForMemberDTO;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public List<MemberListForAdminDTO> findAllForAdmin() {
        return findAll(member -> true, MemberListForAdminDTO::new);
    }

    @Transactional(readOnly = true)
    public List<MemberListForMemberDTO> findAllForMember(String currentUsername) {
        return findAll(member -> !member.getUser().getUsername().equals(currentUsername), MemberListForMemberDTO::new);
    }

    @Transactional(readOnly = true)
    public Member findMemberByUser(String username) {
        return memberRepository.findMemberByUser(username)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "username", username));
    }

    @Transactional(readOnly = true)
    public Member findMemberByUser(int userId) {
        return memberRepository.findMemberByUser(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", userId));
    }

    @Transactional(readOnly = true)
    public Member findMember(Integer memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", memberId));
    }

    @Transactional(readOnly = true)
    public MemberDTO findMemberDTO(Integer memberId) {
        return memberRepository.findById(memberId)
                .map(MemberDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", memberId));
    }

    @Transactional(readOnly = true)
    public MemberForMemberDTO findMemberForMemberDTO(Integer memberId) {
        return memberRepository.findById(memberId)
                .map(MemberForMemberDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", memberId));
    }

    @Transactional(readOnly = true)
    public Member findMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "email", email));
    }

    @Transactional
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public Boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    private <T> List<T> findAll(Predicate<Member> filter, Function<Member, T> mapper) {
        return StreamSupport
            .stream(memberRepository.findAll().spliterator(), false)
            .filter(filter)
            .sorted(Comparator.comparing(Member::getFullName, String.CASE_INSENSITIVE_ORDER))
            .map(mapper)
            .toList();
    }

    @Transactional
    public void delete(Integer memberId) {
        memberRepository.deleteById(memberId);
    }

}
