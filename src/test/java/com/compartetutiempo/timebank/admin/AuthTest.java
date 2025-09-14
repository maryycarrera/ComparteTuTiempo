package com.compartetutiempo.timebank.admin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.compartetutiempo.timebank.BaseTest;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthTest extends BaseTest {

    private static final String BASE_URL = "/api/v1/auth";
    private static final String PROFILE_URL = BASE_URL + "/profile";
    private static final String FULLNAME_URL = BASE_URL + "/fullname";

    @Autowired
    private MockMvc mockMvc;

    private String adminToken;

    @BeforeEach
    void authenticateAdmin() throws Exception {
        adminToken = getToken("admin1", "sys4dm1n*!");
    }

    @Test
    public void shouldReturnAdminProfile() throws Exception {
        mockMvc.perform(get(PROFILE_URL)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Administrador"))
                .andExpect(jsonPath("$.lastName").value("del Sistema"))
                .andExpect(jsonPath("$.username").value("admin1"))
                .andExpect(jsonPath("$.email").value("admin1@example.com"))
                .andExpect(jsonPath("$.profilePic").value("profilepics/purple.png"));
    }

    @Test
    public void shouldReturnAdminFullName() throws Exception {
        mockMvc.perform(get(FULLNAME_URL)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object").value("Administrador del Sistema"));
    }

}
