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
public class AdminTest {

    private static final String BASE_URL = "/api/v1/admins";
    private static final String PROFILE_PIC_URL = BASE_URL + "/profile-picture";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldFailGetAllAdminsWhenUnauthenticated() throws Exception {
        mockMvc.perform(get(BASE_URL))
               .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldFailDeleteAdminWhenUnauthenticated() throws Exception {
        int adminId = 1;

        mockMvc.perform(delete(BASE_URL + "/" + adminId))
               .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldFailGetAdminWhenUnauthenticated() throws Exception {
        int adminId = 1;

        mockMvc.perform(get(BASE_URL + "/" + adminId))
               .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldFailUpdateProfileFromAdminEndpointWhenUnauthenticated() throws Exception {
        String updateJson = """
        {
            "name": "Perfil",
            "lastName": "Actualizado"
        }
        """;

        mockMvc.perform(put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldFailUpdateProfilePictureFromAdminEndpointWhenUnauthenticated() throws Exception {
        String newColor = "blue";

        mockMvc.perform(put(PROFILE_PIC_URL)
                .param("color", newColor)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

}
