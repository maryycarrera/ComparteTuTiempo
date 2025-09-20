package com.compartetutiempo.timebank.member;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
public class MemberTest extends BaseTest {

    private static final String BASE_URL = "/api/v1/members";
    private static final String PROFILE_PIC_URL = BASE_URL + "/profile-picture";

    @Autowired
    private MockMvc mockMvc;

    private String memberToken;

    @BeforeEach
    void authenticateMember() throws Exception {
        memberToken = getToken("member2", "m13mbr0CTT*");
    }

    @Test
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

    @Test
    public void shouldFindMemberByIdSuccessfully() throws Exception {
        int memberId = 1;

        mockMvc.perform(get(BASE_URL + "/" + memberId)
                .header("Authorization", "Bearer " + memberToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Miembro con ID " + memberId + " encontrado con éxito."))
                .andExpect(jsonPath("$.object.fullName").value("John Doe"))
                .andExpect(jsonPath("$.object.username").value("member1"))
                .andExpect(jsonPath("$.object.profilePic").value("profilepics/blue.png"))
                .andExpect(jsonPath("$.object.biography").value("Hola, soy John Doe. Me gusta cocinar y jugar al baloncesto."))
                .andExpect(jsonPath("$.object.dateOfMembership").value("16/01/2022"))
                .andExpect(jsonPath("$.object.hours").value("4"))
                .andExpect(jsonPath("$.object.minutes").value("30"));
    }

    @Test
    public void shouldReturnSameMemberMessage() throws Exception {
        int memberId = 2;

        mockMvc.perform(get(BASE_URL + "/" + memberId)
                .header("Authorization", "Bearer " + memberToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("El miembro con ID " + memberId + " eres tú. Debes usar el endpoint /api/v1/auth/profile para ver tu perfil."));
    }

    @Test
    public void shouldReturnNotFoundForNonExistentMember() throws Exception {
        int nonExistentMemberId = 9999;

        mockMvc.perform(get(BASE_URL + "/" + nonExistentMemberId)
                .header("Authorization", "Bearer " + memberToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Member not found with id: '" + nonExistentMemberId + "'"));
    }

    @Test
    public void shouldFailDeleteMemberWhenUserIsMember() throws Exception {
        int memberId = 1;

        mockMvc.perform(delete(BASE_URL + "/" + memberId)
                .header("Authorization", "Bearer " + memberToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    public void shouldUpdateMemberSuccessfully() throws Exception {
        String updateJson = """
        {
            "name": "Miembro",
            "lastName": "Actualizado",
            "biography": "Biografía actualizada."
        }
        """;

        mockMvc.perform(put(BASE_URL)
                .header("Authorization", "Bearer " + memberToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Perfil actualizado con éxito."))
                .andExpect(jsonPath("$.object.name").value("Miembro"))
                .andExpect(jsonPath("$.object.lastName").value("Actualizado"))
                .andExpect(jsonPath("$.object.username").value("member2"))
                .andExpect(jsonPath("$.object.email").value("member2@example.com"))
                .andExpect(jsonPath("$.object.profilePic").value("profilepics/gray.png"))
                .andExpect(jsonPath("$.object.biography").value("Biografía actualizada."))
                .andExpect(jsonPath("$.object.dateOfMembership").value("04/02/2022"))
                .andExpect(jsonPath("$.object.hours").value("-5"))
                .andExpect(jsonPath("$.object.minutes").value("0"));
    }

    @Test
    public void shouldFailToUpdateMemberWhenDataIsInvalid() throws Exception {
        String updateJson = """
        {
            "name": "",
            "lastName": "ho",
            "biography": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut at vehicula est. Quisque aliquam felis ante, at ultrices enim hendrerit id. Proin velit lorem, faucibus sed lobortis quis, scelerisque eget ipsum. Praesent orci mi, suscipit et purus ac, sollicitudin bibendum mi. Ut id magna eu ipsum accumsan tempor. Donec scelerisque ultrices dignissim. Aliquam elit justo, luctus id laoreet eu, porta vel massa. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Integer nec lectus feugiat, consectetur ex eget, feugiat magna. Cras blandit magna eget dolor tristique, sit amet convallis nunc lobortis. Suspendisse potenti."
        }
        """;

        mockMvc.perform(put(BASE_URL)
                .header("Authorization", "Bearer " + memberToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void shouldUpdateProfilePictureSuccessfully() throws Exception {
        String newColor = "blue";

        mockMvc.perform(put(PROFILE_PIC_URL)
                .header("Authorization", "Bearer " + memberToken)
                .param("color", newColor)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Foto de perfil actualizada con éxito."))
                .andExpect(jsonPath("$.object.name").value("Jane"))
                .andExpect(jsonPath("$.object.lastName").value("Smith"))
                .andExpect(jsonPath("$.object.username").value("member2"))
                .andExpect(jsonPath("$.object.email").value("member2@example.com"))
                .andExpect(jsonPath("$.object.profilePic").value("profilepics/blue.png"))
                .andExpect(jsonPath("$.object.biography").value("H"))
                .andExpect(jsonPath("$.object.dateOfMembership").value("04/02/2022"))
                .andExpect(jsonPath("$.object.hours").value("-5"))
                .andExpect(jsonPath("$.object.minutes").value("0"));
    }

    @Test
    public void shouldFailToUpdateProfilePictureWithInvalidColor() throws Exception {
        String invalidColor = "black";

        mockMvc.perform(put(PROFILE_PIC_URL)
                .header("Authorization", "Bearer " + memberToken)
                .param("color", invalidColor)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The color 'black' is not a valid profile picture color for action: 'updateProfilePicture'"));

        invalidColor = "turquoise";
        mockMvc.perform(put(PROFILE_PIC_URL)
                .header("Authorization", "Bearer " + memberToken)
                .param("color", invalidColor)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The color 'turquoise' is not a valid profile picture color for action: 'updateProfilePicture'"));
    }

    @Test
    public void shouldFailToUpdateProfilePictureWithEmptyColor() throws Exception {
        String emptyColor = "   ";

        mockMvc.perform(put(PROFILE_PIC_URL)
                .header("Authorization", "Bearer " + memberToken)
                .param("color", emptyColor)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El color de la imagen de perfil no puede estar vacío."));
    }

}
