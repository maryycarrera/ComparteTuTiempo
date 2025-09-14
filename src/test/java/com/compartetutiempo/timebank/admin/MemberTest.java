package com.compartetutiempo.timebank.admin;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;

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

    private String adminToken;

    @BeforeEach
    void authenticateAdmin() throws Exception {
        adminToken = getToken("admin2", "sys4dm1n*!");
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

}
