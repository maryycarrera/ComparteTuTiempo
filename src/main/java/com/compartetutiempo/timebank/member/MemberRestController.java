package com.compartetutiempo.timebank.member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compartetutiempo.timebank.payload.response.ListMessageResponse;

@RestController
@RequestMapping("/api/v1/members")
public class MemberRestController {

    private final MemberService memberService;

    @Autowired
    public MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<ListMessageResponse<Member>> findAllMembers() {
        List<Member> members = memberService.findAll();
        if (members.isEmpty()) {
            ListMessageResponse<Member> response = new ListMessageResponse<Member>("¡Vaya! Parece que no hay miembros registrados aún.");
            return ResponseEntity.ok(response);
        }
        ListMessageResponse<Member> response = new ListMessageResponse<Member>("Lista de miembros encontrada con éxito.", members);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "{memberId}")
    public ResponseEntity<Member> findById(@PathVariable("memberId") Integer memberId) {
        Member member = memberService.findMember(memberId);
        return ResponseEntity.ok(member);
    }

}
