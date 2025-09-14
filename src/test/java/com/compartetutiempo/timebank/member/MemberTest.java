package com.compartetutiempo.timebank.member;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberTest {

    private static final String BASE_URL = "/api/v1/members";

    @Autowired
    private MockMvc mockMvc;

    private String memberToken;

    @BeforeEach
    void authenticateMember() throws Exception {
        memberToken = getMemberToken();
    }

    private String getMemberToken() throws Exception {
        String loginJson = """
        {
            "username": "member1",
            "password": "m13mbr0CTT*"
        }
        """;

        String loginResponse = mockMvc.perform(post("/api/v1/auth/login")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(loginJson))
                                        .andReturn().getResponse().getContentAsString();

        return JsonPath.read(loginResponse, "$.token");
    }

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

}
