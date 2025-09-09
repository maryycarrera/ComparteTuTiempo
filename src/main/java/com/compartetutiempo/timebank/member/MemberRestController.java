package com.compartetutiempo.timebank.member;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
public class MemberRestController {

    private final MemberService memberService;

    @Autowired
    public MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<Member>> findAllMembers() {
        List<Member> members = StreamSupport
                .stream(memberService.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return ResponseEntity.ok(members);
    }

    @GetMapping(value = "{memberId}")
    public ResponseEntity<Member> findById(@PathVariable("memberId") Integer memberId) {
        Member member = memberService.findMember(memberId);
        return ResponseEntity.ok(member);
    }

}
