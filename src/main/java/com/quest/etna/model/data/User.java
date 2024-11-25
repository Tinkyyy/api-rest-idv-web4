package com.quest.etna.model.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.quest.etna.dto.UserDto;
import com.quest.etna.model.BaseModel;
import com.quest.etna.model.Role;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "users")
@DynamicUpdate()
public class User extends BaseModel {
    @JsonIgnore
    @Column(length = 45, nullable = false)
    private String lastname;
    @JsonIgnore
    @Column(length = 45, nullable = false)
    private String firstname;
    @JsonIgnore
    @Column(length = 45, nullable = false, unique = true)
    private String username;

    @Column(length = 150, nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "varchar(255) default 'USER'")
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Column(columnDefinition = "float default 0")
    private float level;

    public User() {

    }

    public User(String lastname, String firstname, String username, String email, String password) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String lastname, String firstname, String username, String email, String password, Role role) {
        this(lastname, firstname, username, email, password);
        this.role = role;
    }

    public User(UserDto userDto) {
        this(userDto.getLastname(), userDto.getFirstname(), userDto.getUsername(), userDto.getEmail(), userDto.getPassword());
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
    @JsonIgnore
    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }
    @JsonIgnore
    public boolean isTeacher() {
        return this.role == Role.TEACHER;
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
