package com.compartetutiempo.timebank.unauthenticated;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthTest {

    private static final String BASE_URL = "/api/v1/auth";
    private static final String PROFILE_URL = BASE_URL + "/profile";
    private static final String FULLNAME_URL = BASE_URL + "/fullname";

    @Autowired
    private MockMvc mockMvc;

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
