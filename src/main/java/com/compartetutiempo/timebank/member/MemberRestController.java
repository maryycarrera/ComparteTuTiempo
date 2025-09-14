package com.compartetutiempo.timebank.member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compartetutiempo.timebank.member.dto.MemberListForAdminDTO;
import com.compartetutiempo.timebank.payload.response.ListMessageResponse;
import com.compartetutiempo.timebank.user.Authority;
import com.compartetutiempo.timebank.user.User;
import com.compartetutiempo.timebank.user.UserService;

@RestController
@RequestMapping("/api/v1/members")
public class MemberRestController {

    private final MemberService memberService;
    private final UserService userService;

    @Autowired
    public MemberRestController(MemberService memberService, UserService userService) {
        this.memberService = memberService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ListMessageResponse<?>> findAllMembers() {
        User currentUser = userService.findCurrentUser();
        Authority authority = currentUser.getAuthority();

        List<?> members = List.of();

        if (authority.equals(Authority.MEMBER)) {
            members = memberService.findAllForMember(currentUser.getUsername());
        } else if (authority.equals(Authority.ADMIN)) {
            members = memberService.findAllForAdmin();
        }

        if (members.isEmpty()) {
            ListMessageResponse<MemberListForAdminDTO> response = new ListMessageResponse<MemberListForAdminDTO>("¡Vaya! Parece que no hay miembros registrados aún.");
            return ResponseEntity.ok(response);
        }

        ListMessageResponse<?> response = new ListMessageResponse<>("Lista de miembros encontrada con éxito.", members);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "{memberId}")
    public ResponseEntity<Member> findById(@PathVariable("memberId") Integer memberId) {
        Member member = memberService.findMember(memberId);
        return ResponseEntity.ok(member);
    }

}
