package com.compartetutiempo.timebank.member;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.compartetutiempo.timebank.BaseTest;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberTest extends BaseTest {

    private static final String BASE_URL = "/api/v1/members";

    @Autowired
    private MockMvc mockMvc;

    private String memberToken;

    @BeforeEach
    void authenticateMember() throws Exception {
        memberToken = getToken("member2", "m13mbr0CTT*");
    }

    @Test
    public void shouldListAllMembersSuccessfully() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .header("Authorization", "Bearer " + memberToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Lista de miembros encontrada con éxito."))
                .andExpect(jsonPath("$.objects").isArray())
                .andExpect(jsonPath("$.objects").isNotEmpty())
                .andExpect(jsonPath("$.objects.length()").value(greaterThanOrEqualTo(10)));
    }

    @Test
    public void shouldFindMemberByIdSuccessfully() throws Exception {
        int memberId = 1;

        mockMvc.perform(get(BASE_URL + "/" + memberId)
                .header("Authorization", "Bearer " + memberToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Miembro con ID " + memberId + " encontrado con éxito."))
                .andExpect(jsonPath("$.object.fullName").value("John Doe"))
                .andExpect(jsonPath("$.object.username").value("member1"))
                .andExpect(jsonPath("$.object.profilePic").value("profilepics/blue.png"))
                .andExpect(jsonPath("$.object.biography").value("Hola, soy John Doe. Me gusta cocinar y jugar al baloncesto."))
                .andExpect(jsonPath("$.object.dateOfMembership").value("16/01/2022"))
                .andExpect(jsonPath("$.object.hours").value("4"))
                .andExpect(jsonPath("$.object.minutes").value("30"));
    }

    @Test
    public void shouldReturnSameMemberMessage() throws Exception {
        int memberId = 2;

        mockMvc.perform(get(BASE_URL + "/" + memberId)
                .header("Authorization", "Bearer " + memberToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("El miembro con ID " + memberId + " eres tú. Debes usar el endpoint /api/v1/auth/profile para ver tu perfil."));
    }

    @Test
    public void shouldReturnNotFoundForNonExistentMember() throws Exception {
        int nonExistentMemberId = 9999;

        mockMvc.perform(get(BASE_URL + "/" + nonExistentMemberId)
                .header("Authorization", "Bearer " + memberToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Member not found with id: '" + nonExistentMemberId + "'"));
    }

    @Test
    public void shouldFailDeleteMemberWhenUserIsMember() throws Exception {
        int memberId = 1;

        mockMvc.perform(delete(BASE_URL + "/" + memberId)
                .header("Authorization", "Bearer " + memberToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}
