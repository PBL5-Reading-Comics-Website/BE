package com.example.readingcomicwebsite.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
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
        List<SimpleGrantedAuthority> roleList = new ArrayList<>();
        for (String role : roles) {
            roleList.add(new SimpleGrantedAuthority(role));
        }
        return roleList;
    }

    public String getPassword() {
        return password;
    }

    public UserDetailsCustome setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserDetailsCustome(Integer id, String username, String... roles) {
        this.id = id;
        this.username = username;
        this.roles = Arrays.asList(roles);
    }

    public UserDetailsCustome(Integer id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = List.of(role);
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
