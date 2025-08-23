package com.compartetutiempo.timebank.auth.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private Integer id;
    private String username;
    private String email;
    private String role;

    public JwtResponse(String token, String username, String email, Integer id, String role) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.id = id;
        this.role = role;
    }

    @Override
    public String toString() {
        return "JwtResponse{" +
                "token='" + token + '\'' +
                ", type='" + type + '\'' +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

}
