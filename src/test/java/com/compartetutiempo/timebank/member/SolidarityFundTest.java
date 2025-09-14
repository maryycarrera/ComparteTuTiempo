package com.compartetutiempo.timebank.member;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
public class SolidarityFundTest extends BaseTest {

    private static final String BASE_URL = "/api/v1/solidarity-fund";

    @Autowired
    private MockMvc mockMvc;

    private String memberToken;

    @BeforeEach
    void authenticateMember() throws Exception {
        memberToken = getToken("member1", "m13mbr0CTT*");
    }

    @Test
    public void shouldFindSolidarityFundSuccessfully() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .header("Authorization", "Bearer " + memberToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Fondo Solidario encontrado con éxito."))
                .andExpect(jsonPath("$.object.hours").value(100))
                .andExpect(jsonPath("$.object.minutes").value(0));
    }

}
