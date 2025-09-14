package com.compartetutiempo.timebank.member;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compartetutiempo.timebank.exceptions.ResourceNotFoundException;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public List<MemberListDTO> findAll() {
        return StreamSupport
                .stream(memberRepository.findAll().spliterator(), false)
                .map(member -> new MemberListDTO(member))
                .toList();
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

}
