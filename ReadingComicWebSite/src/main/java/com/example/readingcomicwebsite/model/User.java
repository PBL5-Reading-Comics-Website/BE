package com.example.readingcomicwebsite.model;

import com.example.readingcomicwebsite.util.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
@Setter
@Getter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //username
    @Column(name = "username")
    private String username;

    //password
    @Column(name = "password")
    private String password;

    //name
    @Column(name = "name")
    private String name;

    //nickname
    @Column(name = "nickname")
    private String nickname;

    //date_of_birth
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    //gender
    @Column(name = "gender")
    private Boolean gender;

    //email
    @Column(name = "email")
    private String email;

    //avatar
    @Column(name = "avatar")
    private String avatar;

    //registration_date
    @Column(name = "registration_date")
    private Date registrationDate;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
