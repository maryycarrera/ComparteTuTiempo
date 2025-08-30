package com.compartetutiempo.timebank.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthTest {

    @Autowired
    private MockMvc mockMvc;

    private static String memberToken;
    private static String adminToken;

    @Test
    @Order(1)
    public void shouldLoginMemberWhenCredentialsAreValid() throws Exception {
        String json = """
        {
            "username": "maryycarrera",
            "password": "m13mbr0CTT"
        }
        """;

        String response = mockMvc.perform(post("/api/v1/auth/login")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                                    .andExpect(status().isOk())
                                    .andExpect(jsonPath("$.token").exists())
                                    .andExpect(jsonPath("$.username").value("maryycarrera"))
                                    .andExpect(jsonPath("$.authorities").isArray())
                                    .andExpect(jsonPath("$.authorities[0]").value("MEMBER"))
                                    .andReturn().getResponse().getContentAsString();

        memberToken = JsonPath.read(response, "$.token");
    }

    @Test
    @Order(2)
    public void shouldLogoutMember() throws Exception {
        String json = String.format("{\"token\": \"%s\"}", memberToken);

        mockMvc.perform(post("/api/v1/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void shouldLoginAdminWhenCredentialsAreValid() throws Exception {
        String json = """
        {
            "username": "admin1",
            "password": "sys4dm1n*!"
        }
        """;

        String response = mockMvc.perform(post("/api/v1/auth/login")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(json))
                                    .andExpect(status().isOk())
                                    .andExpect(jsonPath("$.token").exists())
                                    .andExpect(jsonPath("$.username").value("admin1"))
                                    .andExpect(jsonPath("$.authorities").isArray())
                                    .andExpect(jsonPath("$.authorities[0]").value("ADMIN"))
                                    .andReturn().getResponse().getContentAsString();

        adminToken = JsonPath.read(response, "$.token");
    }

    @Test
    @Order(4)
    public void shouldLogoutAdmin() throws Exception {
        String json = String.format("{\"token\": \"%s\"}", adminToken);

        mockMvc.perform(post("/api/v1/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
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
