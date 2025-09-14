package com.compartetutiempo.timebank.member;

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

    private String memberToken;

    @BeforeEach
    void authenticateMember() throws Exception {
        memberToken = getToken("member1", "m13mbr0CTT*");
    }

    @Test
    public void shouldReturnMemberProfileWhenLoggedIn() throws Exception {
        mockMvc.perform(get(PROFILE_URL)
                .header("Authorization", "Bearer " + memberToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.username").value("member1"))
                .andExpect(jsonPath("$.email").value("member1@example.com"))
                .andExpect(jsonPath("$.profilePic").value("profilepics/blue.png"))
                .andExpect(jsonPath("$.biography").value("Hola, soy John Doe. Me gusta cocinar y jugar al baloncesto."))
                .andExpect(jsonPath("$.dateOfMembership").value("16/01/2022"))
                .andExpect(jsonPath("$.hours").value("4"))
                .andExpect(jsonPath("$.minutes").value("30"));
    }

    @Test
    public void shouldReturnMemberFullName() throws Exception {
        mockMvc.perform(get(FULLNAME_URL)
                .header("Authorization", "Bearer " + memberToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object").value("John Doe"));
    }

}
