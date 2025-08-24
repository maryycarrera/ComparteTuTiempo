package com.compartetutiempo.timebank.auth.payload.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private Integer id;
    private String username;
    private List<String> authorities;

    public JwtResponse(String token, String username, Integer id, List<String> authorities) {
        this.token = token;
        this.username = username;
        this.id = id;
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "JwtResponse{" +
                "token='" + token + '\'' +
                ", type='" + type + '\'' +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", authorities='" + authorities + '\'' +
                '}';
    }

}
