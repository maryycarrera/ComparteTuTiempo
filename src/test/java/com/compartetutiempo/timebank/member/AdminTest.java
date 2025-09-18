package com.compartetutiempo.timebank.member;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
public class AdminTest extends BaseTest {

    private static final String BASE_URL = "/api/v1/admins";
    private static final String PROFILE_PIC_URL = BASE_URL + "/profile-picture";

    @Autowired
    private MockMvc mockMvc;

    private String memberToken;

    @BeforeEach
    void authenticateMember() throws Exception {
        memberToken = getToken("member1", "m13mbr0CTT*");
    }

    @Test
    public void shouldFailGetAllAdminsWhenUserIsMember() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .header("Authorization", "Bearer " + memberToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldFailDeleteAdminWhenUserIsMember() throws Exception {
        int adminId = 1;

        mockMvc.perform(delete(BASE_URL + "/" + adminId)
                .header("Authorization", "Bearer " + memberToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldFailGetAdminWhenUserIsMember() throws Exception {
        int adminId = 1;

        mockMvc.perform(get(BASE_URL + "/" + adminId)
                .header("Authorization", "Bearer " + memberToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldFailUpdateProfileFromAdminEndpointWhenUserIsMember() throws Exception {
        String updateJson = """
        {
            "name": "Perfil",
            "lastName": "Actualizado"
        }
        """;

        mockMvc.perform(put(BASE_URL)
                .header("Authorization", "Bearer " + memberToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldFailUpdateProfilePictureFromAdminEndpointWhenUserIsMember() throws Exception {
        String newColor = "blue";

        mockMvc.perform(put(PROFILE_PIC_URL)
                .header("Authorization", "Bearer " + memberToken)
                .param("color", newColor)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}
