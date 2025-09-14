package com.compartetutiempo.timebank;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.JsonPath;

public abstract class BaseTest {

    @Autowired
    protected MockMvc mockMvc;

    protected String getToken(String username, String password) throws Exception {
        String loginJson = String.format("""
        {
            "username": "%s",
            "password": "%s"
        }
        """, username, password);

        String loginResponse = mockMvc.perform(post("/api/v1/auth/login")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(loginJson))
                                        .andReturn().getResponse().getContentAsString();

        return JsonPath.read(loginResponse, "$.token");
    }

}
