package com.compartetutiempo.timebank.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
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

    private static final String BASE_URL = "/api/v1/auth";
    private static final String LOGIN_URL = BASE_URL + "/login";
    private static final String LOGOUT_URL = BASE_URL + "/logout";
    private static final String PROFILE_URL = BASE_URL + "/profile";
    private static final String SIGNUP_URL = BASE_URL + "/signup";
    private static final String FULLNAME_URL = BASE_URL + "/fullname";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldLoginAndLogoutMember() throws Exception {
        String loginJson = """
        {
            "username": "maryycarrera",
            "password": "m13mbr0CTT*"
        }
        """;

        String loginResponse = mockMvc.perform(post(LOGIN_URL)
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
        mockMvc.perform(post(LOGOUT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(logoutJson))
                .andExpect(status().isOk());

        mockMvc.perform(get(PROFILE_URL)
                .header("Authorization", "Bearer " + memberToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldLoginAndLogoutAdmin() throws Exception {
        String loginJson = """
        {
            "username": "admin1",
            "password": "sys4dm1n*!"
        }
        """;

        String loginResponse = mockMvc.perform(post(LOGIN_URL)
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
        mockMvc.perform(post(LOGOUT_URL)
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

        mockMvc.perform(post(LOGIN_URL)
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

        mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFailLoginWhenCredentialsAreNotProvided() throws Exception {
        String json = "{}";

        mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRegisterMemberSuccessfully() throws Exception {
        String signupJson = """
        {
            "name": "Juan",
            "lastName": "Pérez",
            "username": "juanperez1",
            "email": "juan.perez1@example.com",
            "password": "seguro123&"
        }
        """;

        mockMvc.perform(post(SIGNUP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Registro exitoso."))
                .andExpect(jsonPath("$.object.hours").value(5))
                .andExpect(jsonPath("$.object.minutes").value(0))
                .andExpect(jsonPath("$.object.biography").value(""))
                .andExpect(jsonPath("$.object.dateOfMembership").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.object.profilePicture").value("profilepics/black.png"));
    }

    @Test
    public void shouldFailRegistrationWhenUsernameAlreadyExists() throws Exception {
        String signupJson = """
        {
            "name": "Taylor",
            "lastName": "Swift",
            "username": "member1",
            "email": "taylor.swift13@example.com",
            "password": "il0vecats89!"
        }
        """;

        mockMvc.perform(post(SIGNUP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("El dato 'Nombre de usuario' con valor 'member1' ya está en uso."));
    }

    @Test
    public void shouldFailRegistrationWhenEmailAlreadyExists() throws Exception {
        String signupJson = """
        {
            "name": "Taylor",
            "lastName": "Swift",
            "username": "taylorswift13",
            "email": "admin1@example.com",
            "password": "il0vecats89!"
        }
        """;

        mockMvc.perform(post(SIGNUP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("El dato 'Dirección de correo electrónico' con valor 'admin1@example.com' ya está en uso."));
    }

    @Test
    public void shouldFailRegistrationWhenDataIsInvalid() throws Exception {
        String signupJson = """
        {
            "name": "Al",
            "lastName": "",
            "username": "a",
            "email": "invalid-email",
            "password": "123"
        }
        """;

        mockMvc.perform(post(SIGNUP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFailProfileRouteWhenNotLoggedIn() throws Exception {
        mockMvc.perform(get(PROFILE_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldFailFullNameRouteWhenNotLoggedIn() throws Exception {
        mockMvc.perform(get(FULLNAME_URL))
                .andExpect(status().isUnauthorized());
    }

}
