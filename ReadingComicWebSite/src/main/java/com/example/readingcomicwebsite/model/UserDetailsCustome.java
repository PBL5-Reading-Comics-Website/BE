package com.example.readingcomicwebsite.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class UserDetailsCustome implements UserDetails {
    Integer id;
    String username;
    private String password;
    private List<String> roles;

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public UserDetailsCustome setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public String getPassword() {
        return password;
    }

    public UserDetailsCustome setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserDetailsCustome(Integer id, String... roles) {
        this.id = id;
        this.roles = Arrays.asList(roles);
    }

    public Integer getId() {
        return id;
    }

    public UserDetailsCustome setId(Integer id) {
        this.id = id;
        return this;
    }

    public List<String> getRoles() {
        return roles;
    }

    public UserDetailsCustome setRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }
}
