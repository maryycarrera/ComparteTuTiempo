package com.compartetutiempo.timebank.admin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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

    @Autowired
    private MockMvc mockMvc;

    private String adminToken;

    @BeforeEach
    void authenticateAdmin() throws Exception {
        adminToken = getToken("admin2", "sys4dm1n*!");
    }

    @Test
    public void shouldCreateAdminSuccessfully() throws Exception {
        String createJson = """
        {
            "name": "Juan",
            "lastName": "Pérez",
            "username": "juanperez2",
            "email": "juan.perez2@example.com",
            "password": "seguro123&"
        }
        """;
        mockMvc.perform(post(BASE_URL)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Registro exitoso."))
                .andExpect(jsonPath("$.object.profilePicture").value("profilepics/black.png"));
    }

    @Test
    public void shouldFailToCreateAdminWithDuplicateUsername() throws Exception {
        String createJson = """
        {
            "name": "Taylor",
            "lastName": "Swift",
            "username": "admin1",
            "email": "taylor.swift13@example.com",
            "password": "il0vecats89!"
        }
        """;
        mockMvc.perform(post(BASE_URL)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("El dato 'Nombre de usuario' con valor 'admin1' ya está en uso."));
    }

    @Test
    public void shouldFailToCreateAdminWithDuplicateEmail() throws Exception {
        String createJson = """
        {
            "name": "Taylor",
            "lastName": "Swift",
            "username": "taylorswift13",
            "email": "member1@example.com",
            "password": "il0vecats89!"
        }
        """;

        mockMvc.perform(post(BASE_URL)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("El dato 'Dirección de correo electrónico' con valor 'member1@example.com' ya está en uso."));
    }

    @Test
    public void shouldFailToCreateAdminWhenPasswordLacksSymbol() throws Exception {
        String createJson = """
        {
            "name": "Taylor",
            "lastName": "Swift",
            "username": "taylorswift13",
            "email": "taylor.swift.13@example.com",
            "password": "il0vecats89"
        }
        """;

        mockMvc.perform(post(BASE_URL)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFailToCreateAdminWhenPasswordLacksNumber() throws Exception {
        String createJson = """
        {
            "name": "Taylor",
            "lastName": "Swift",
            "username": "taylorswift13",
            "email": "taylor.swift.13@example.com",
            "password": "ilovecats!"
        }
        """;

        mockMvc.perform(post(BASE_URL)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFailToCreateAdminWhenPasswordLacksLowercase() throws Exception {
        String createJson = """
        {
            "name": "Taylor",
            "lastName": "Swift",
            "username": "taylorswift13",
            "email": "taylor.swift.13@example.com",
            "password": "IL0VECATS89!"
        }
        """;

        mockMvc.perform(post(BASE_URL)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFailToCreateAdminWhenUsernameHasUppercase() throws Exception {
        String createJson = """
        {
            "name": "Taylor",
            "lastName": "Swift",
            "username": "TaylorSwift13",
            "email": "taylor.swift.13@example.com",
            "password": "il0vecats89!"
        }
        """;

        mockMvc.perform(post(BASE_URL)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFailToCreateAdminWhenUsernameHasInvalidChars() throws Exception {
        String createJson = """
        {
            "name": "Taylor",
            "lastName": "Swift",
            "username": "taylorswift-13!",
            "email": "taylor.swift.13@example.com",
            "password": "il0vecats89!"
        }
        """;

        mockMvc.perform(post(BASE_URL)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFailToCreateAdminWhenDataIsInvalid() throws Exception {
        String signupJson = """
        {
            "name": "Al",
            "lastName": "",
            "username": "a",
            "email": "invalid-email",
            "password": "123"
        }
        """;

        mockMvc.perform(post(BASE_URL)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupJson))
                .andExpect(status().isBadRequest());
    }

}
