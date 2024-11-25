package com.quest.etna.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quest.etna.model.Role;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class UserDto {

    @NotEmpty
    @NotNull
    private String lastname;

    @NotEmpty
    @NotNull
    private String firstname;

    @NotEmpty
    @NotNull
    private String username;

    @NotEmpty
    @NotNull
    @Email
    private String email;

    @NotEmpty
    private String password;

    private Role role;

    private float level;

    public UserDto() {

    }

    public UserDto(String lastname, String firstname, String username, String email, String password, float level) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.level = level;
    }

    public UserDto(String lastname, String firstname, String username, String email, String password, float level, Role role) {
        this(lastname, firstname, username, email, password, level);
        this.role = role;
    }
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public float getLevel() {
        return level;
    }

    public void setLevel(float level) {
        this.level = level;
    }
}
