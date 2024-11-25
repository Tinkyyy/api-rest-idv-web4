package com.quest.etna.dto;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class AuthenticationDto {

    @NotEmpty
    @NotNull
    private String username;

    @NotEmpty
    private String password;


    public AuthenticationDto() {

    }

    public AuthenticationDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
