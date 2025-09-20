package com.compartetutiempo.timebank.member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.compartetutiempo.timebank.member.dto.MemberDTO;
import com.compartetutiempo.timebank.member.dto.MemberEditDTO;
import com.compartetutiempo.timebank.member.dto.MemberForMemberDTO;
import com.compartetutiempo.timebank.member.dto.MemberListForAdminDTO;
import com.compartetutiempo.timebank.member.dto.MemberProfileDTO;
import com.compartetutiempo.timebank.payload.response.ListMessageResponse;
import com.compartetutiempo.timebank.payload.response.MessageResponse;
import com.compartetutiempo.timebank.user.Authority;
import com.compartetutiempo.timebank.user.User;
import com.compartetutiempo.timebank.user.UserService;

import jakarta.validation.Valid;

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
    public ResponseEntity<MessageResponse<?>> findById(@PathVariable("memberId") Integer memberId) {
        User currentUser = userService.findCurrentUser();
        Authority authority = currentUser.getAuthority();

        if (authority.equals(Authority.MEMBER)) {
            Member currentMember = memberService.findMemberByUser(currentUser.getUsername());
            if (currentMember.getId().equals(memberId)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse<>("El miembro con ID " + memberId + " eres tú. Debes usar el endpoint /api/v1/auth/profile para ver tu perfil."));
            }
            MemberForMemberDTO member = memberService.findMemberForMemberDTO(memberId);
            return ResponseEntity.ok(new MessageResponse<MemberForMemberDTO>("Miembro con ID " + memberId + " encontrado con éxito.", member));
        }

        MemberDTO member = memberService.findMemberDTO(memberId);
        return ResponseEntity.ok(new MessageResponse<MemberDTO>("Miembro con ID " + memberId + " encontrado con éxito.", member));
    }

    @PutMapping()
    public ResponseEntity<MessageResponse<MemberProfileDTO>> update(@Valid @RequestBody MemberEditDTO memberEditDTO) {
        User currentUser = userService.findCurrentUser();
        MemberProfileDTO updatedMember = memberService.updateByUsername(currentUser.getUsername(), memberEditDTO);
        return ResponseEntity.ok(new MessageResponse<>("Perfil actualizado con éxito.", updatedMember));
    }

    @PutMapping("/profile-picture")
    public ResponseEntity<MessageResponse<MemberProfileDTO>> updateProfilePicture(@RequestParam String color) {
        User currentUser = userService.findCurrentUser();
        MemberProfileDTO updatedMember = memberService.updateProfilePicture(currentUser.getUsername(), color);
        return ResponseEntity.ok(new MessageResponse<>("Foto de perfil actualizada con éxito.", updatedMember));
    }

    @DeleteMapping(value = "{memberId}")
    public ResponseEntity<String> delete(@PathVariable("memberId") Integer memberId) {
        memberService.delete(memberId);
        return ResponseEntity.ok("Miembro con ID " + memberId + " eliminado con éxito.");
    }

}
