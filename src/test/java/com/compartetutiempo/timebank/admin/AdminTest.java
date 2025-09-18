package com.compartetutiempo.timebank.admin;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.transaction.annotation.Transactional;

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
        adminToken = getToken("admin2", "Sys4dm1n*!");
    }

    @Test
    public void shouldCreateAdminSuccessfully() throws Exception {
        String createJson = """
        {
            "name": "Juan",
            "lastName": "Pérez",
            "username": "juanperez2",
            "email": "juan.perez2@example.com",
            "password": "Seguro123&"
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
            "password": "il0vecaTS89!"
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
            "password": "il0vecaTS89!"
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
            "password": "il0vecaTS89"
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
            "password": "ilovecaTS!"
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
    public void shouldFailToCreateAdminWhenPasswordLacksUppercase() throws Exception {
        String createJson = """
        {
            "name": "Taylor",
            "lastName": "Swift",
            "username": "taylorswift13",
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
    public void shouldFailToCreateAdminWhenPasswordHasWhiteSpace() throws Exception {
        String createJson = """
        {
            "name": "Taylor",
            "lastName": "Swift",
            "username": "taylorswift13",
            "email": "taylor.swift.13@example.com",
            "password": "il0ve caTS89!"
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
            "password": "il0vecaTS89!"
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
            "password": "il0vecaTS89!"
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

    @Test
    public void shouldGetAllAdminsSuccessfully() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Lista de administradores encontrada con éxito."))
                .andExpect(jsonPath("$.objects").isArray())
                .andExpect(jsonPath("$.objects").isNotEmpty())
                .andExpect(jsonPath("$.objects.length()").value(greaterThanOrEqualTo(4)));
    }

    @Test
    @Transactional
    public void shouldDeleteAdminSuccessfully() throws Exception {
        int adminIdToDelete = 4;

        mockMvc.perform(delete(BASE_URL + "/" + adminIdToDelete)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Administrador con ID " + adminIdToDelete + " eliminado con éxito."));

        mockMvc.perform(get(BASE_URL + "/" + adminIdToDelete)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Administrator not found with id: '" + adminIdToDelete + "'"));
    }

    @Test
    public void shouldFailToDeleteSelfAdmin() throws Exception {
        int selfAdminId = 2;

        mockMvc.perform(delete(BASE_URL + "/" + selfAdminId)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Un administrador no puede eliminarse a sí mismo."));
    }

    @Test
    public void shouldReturnNotFoundWhenDeletingNonExistentAdmin() throws Exception {
        int nonExistentAdminId = 9999;

        mockMvc.perform(delete(BASE_URL + "/" + nonExistentAdminId)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Administrator not found with id: '" + nonExistentAdminId + "'"));
    }

    @Test
    public void shouldFindAdminByIdSuccessfully() throws Exception {
        int adminId = 1;

        mockMvc.perform(get(BASE_URL + "/" + adminId)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Administrador con ID " + adminId + " encontrado con éxito."))
                .andExpect(jsonPath("$.object.name").value("Administrador"))
                .andExpect(jsonPath("$.object.lastName").value("del Sistema"))
                .andExpect(jsonPath("$.object.username").value("admin1"))
                .andExpect(jsonPath("$.object.email").value("admin1@example.com"))
                .andExpect(jsonPath("$.object.profilePic").value("profilepics/purple.png"));
    }

    @Test
    public void shouldReturnNotFoundForNonExistentAdmin() throws Exception {
        int nonExistentAdminId = 9999;

        mockMvc.perform(get(BASE_URL + "/" + nonExistentAdminId)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Administrator not found with id: '" + nonExistentAdminId + "'"));
    }

    @Test
    public void shouldReturnSameAdminMessage() throws Exception {
        int adminId = 2;

        mockMvc.perform(get(BASE_URL + "/" + adminId)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("El administrador con ID " + adminId + " eres tú. Debes usar el endpoint /api/v1/auth/profile para ver tu perfil."));
    }

}
