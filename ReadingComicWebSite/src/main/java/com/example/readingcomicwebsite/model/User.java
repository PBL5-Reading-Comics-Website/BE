package com.example.readingcomicwebsite.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "user")
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    //role
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    //registration_date
    @Column(name = "registration_date")
    private Date registrationDate;

}
