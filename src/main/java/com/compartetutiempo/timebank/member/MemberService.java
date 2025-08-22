package com.compartetutiempo.timebank.member;

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
    public Iterable<Member> findAll() {
        return memberRepository.findAll();
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

}
