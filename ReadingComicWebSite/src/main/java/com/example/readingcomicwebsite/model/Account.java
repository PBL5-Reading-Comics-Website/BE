package com.example.readingcomicwebsite.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "account")
@Setter
@Getter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //username
    @Column(name = "username")
    private String username;

    //password
    @Column(name = "password")
    private String password;

    //user
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    //role
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;


}
