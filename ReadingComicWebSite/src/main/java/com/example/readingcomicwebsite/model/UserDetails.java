package com.example.readingcomicwebsite.model;

import java.util.Arrays;
import java.util.List;

public class UserDetails {
    private Integer id;
    private String username;
    private String password;
    private List<String> roles;

    public String getUsername() {
        return username;
    }

    public UserDetails setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserDetails setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserDetails(Integer id, String... roles) {
        this.id = id;
        this.roles = Arrays.asList(roles);
    }

    public Integer getId() {
        return id;
    }

    public UserDetails setId(Integer id) {
        this.id = id;
        return this;
    }

    public List<String> getRoles() {
        return roles;
    }

    public UserDetails setRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }
}
