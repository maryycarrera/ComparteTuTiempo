package com.compartetutiempo.timebank.admin;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.compartetutiempo.timebank.BaseTest;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberTest extends BaseTest {

    private static final String BASE_URL = "/api/v1/members";
    private static final String PROFILE_PIC_URL = BASE_URL + "/profile-picture";

    @Autowired
    private MockMvc mockMvc;

    private String adminToken;

    @BeforeEach
    void authenticateAdmin() throws Exception {
        adminToken = getToken("admin2", "Sys4dm1n*!");
    }

    @Test
    public void shouldListAllMembersSuccessfully() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Lista de miembros encontrada con éxito."))
                .andExpect(jsonPath("$.objects").isArray())
                .andExpect(jsonPath("$.objects").isNotEmpty())
                .andExpect(jsonPath("$.objects.length()").value(greaterThanOrEqualTo(11)));
    }

    @Test
    public void shouldFindMemberByIdSuccessfully() throws Exception {
        int memberId = 1;

        mockMvc.perform(get(BASE_URL + "/" + memberId)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Miembro con ID " + memberId + " encontrado con éxito."))
                .andExpect(jsonPath("$.object.fullName").value("John Doe"))
                .andExpect(jsonPath("$.object.username").value("member1"))
                .andExpect(jsonPath("$.object.email").value("member1@example.com"))
                .andExpect(jsonPath("$.object.profilePic").value("profilepics/blue.png"))
                .andExpect(jsonPath("$.object.biography").value("Hola, soy John Doe. Me gusta cocinar y jugar al baloncesto."))
                .andExpect(jsonPath("$.object.dateOfMembership").value("16/01/2022"))
                .andExpect(jsonPath("$.object.hours").value("4"))
                .andExpect(jsonPath("$.object.minutes").value("30"));
    }

    @Test
    public void shouldReturnNotFoundForNonExistentMember() throws Exception {
        int nonExistentMemberId = 9999;

        mockMvc.perform(get(BASE_URL + "/" + nonExistentMemberId)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Member not found with id: '" + nonExistentMemberId + "'"));
    }

    @Test
    @Transactional
    public void shouldDeleteMemberSuccessfully() throws Exception {
        int memberIdToDelete = 11;

        mockMvc.perform(delete(BASE_URL + "/" + memberIdToDelete)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Miembro con ID " + memberIdToDelete + " eliminado con éxito."));

        mockMvc.perform(get(BASE_URL + "/" + memberIdToDelete)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Member not found with id: '" + memberIdToDelete + "'"));
    }

    @Test
    public void shouldReturnNotFoundWhenDeletingNonExistentMember() throws Exception {
        int nonExistentMemberId = 9999;

        mockMvc.perform(delete(BASE_URL + "/" + nonExistentMemberId)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Member not found with id: '" + nonExistentMemberId + "'"));
    }

    @Test
    public void shouldFailUpdateProfileFromMemberEndpointWhenUserIsAdmin() throws Exception {
        String updateJson = """
        {
            "name": "Perfil",
            "lastName": "Actualizado",
            "biography": "Biografía actualizada desde el endpoint de miembro."
        }
        """;

        mockMvc.perform(put(BASE_URL)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldFailUpdateProfilePictureFromMemberEndpointWhenUserIsAdmin() throws Exception {
        String newColor = "blue";

        mockMvc.perform(put(PROFILE_PIC_URL)
                .header("Authorization", "Bearer " + adminToken)
                .param("color", newColor)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}
