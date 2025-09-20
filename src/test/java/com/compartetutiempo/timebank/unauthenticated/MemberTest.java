package com.compartetutiempo.timebank.unauthenticated;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberTest {

    private static final String BASE_URL = "/api/v1/members";
    private static final String PROFILE_PIC_URL = BASE_URL + "/profile-picture";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldFailGetAllMembersWhenUnauthenticated() throws Exception {
        mockMvc.perform(get(BASE_URL))
               .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldFailGetMemberByIdWhenUnauthenticated() throws Exception {
        int memberId = 1;

        mockMvc.perform(get(BASE_URL + "/" + memberId))
               .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldFailDeleteMemberWhenUnauthenticated() throws Exception {
        int memberId = 1;

        mockMvc.perform(delete(BASE_URL + "/" + memberId))
               .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldFailUpdateProfileFromMemberEndpointWhenUnauthenticated() throws Exception {
        String updateJson = """
        {
            "name": "Perfil",
            "lastName": "Actualizado",
            "biography": "Biografía actualizada desde el endpoint de miembro."
        }
        """;

        mockMvc.perform(put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldFailUpdateProfilePictureFromMemberEndpointWhenUnauthenticated() throws Exception {
        String newColor = "blue";

        mockMvc.perform(put(PROFILE_PIC_URL)
                .param("color", newColor)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

}
