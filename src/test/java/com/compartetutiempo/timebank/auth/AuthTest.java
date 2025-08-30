package com.compartetutiempo.timebank.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldLoginAndLogoutMember() throws Exception {
        String loginJson = """
        {
            "username": "maryycarrera",
            "password": "m13mbr0CTT"
        }
        """;

        String loginResponse = mockMvc.perform(post("/api/v1/auth/login")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(loginJson))
                                    .andExpect(status().isOk())
                                    .andExpect(jsonPath("$.token").exists())
                                    .andExpect(jsonPath("$.username").value("maryycarrera"))
                                    .andExpect(jsonPath("$.authorities").isArray())
                                    .andExpect(jsonPath("$.authorities[0]").value("MEMBER"))
                                    .andReturn().getResponse().getContentAsString();

        String memberToken = JsonPath.read(loginResponse, "$.token");

        String logoutJson = String.format("{\"token\": \"%s\"}", memberToken);
        mockMvc.perform(post("/api/v1/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(logoutJson))
                .andExpect(status().isOk());

        // Intentar acceder con el token después del logout (opcional, si tienes endpoint protegido)
        // mockMvc.perform(get("/api/v1/protected-endpoint")
        //         .header("Authorization", "Bearer " + memberToken))
        //         .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldLoginAndLogoutAdmin() throws Exception {
        String loginJson = """
        {
            "username": "admin1",
            "password": "sys4dm1n*!"
        }
        """;

        String loginResponse = mockMvc.perform(post("/api/v1/auth/login")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(loginJson))
                                    .andExpect(status().isOk())
                                    .andExpect(jsonPath("$.token").exists())
                                    .andExpect(jsonPath("$.username").value("admin1"))
                                    .andExpect(jsonPath("$.authorities").isArray())
                                    .andExpect(jsonPath("$.authorities[0]").value("ADMIN"))
                                    .andReturn().getResponse().getContentAsString();

        String adminToken = JsonPath.read(loginResponse, "$.token");

        String logoutJson = String.format("{\"token\": \"%s\"}", adminToken);
        mockMvc.perform(post("/api/v1/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(logoutJson))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldFailLoginWhenCredentialsAreInvalid() throws Exception {
        String json = """
        {
            "username": "invaliduser",
            "password": "wrongpassword"
        }
        """;

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFailLoginWhenCredentialsAreEmpty() throws Exception {
        String json = """
        {
            "username": "",
            "password": ""
        }
        """;

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFailLoginWhenCredentialsAreNotProvided() throws Exception {
        String json = "{}";

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

}
