package com.quest.etna.model;

import java.io.Serializable;

public class UserDetails implements Serializable {

    private String username;
    private Role role;

    public UserDetails(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
