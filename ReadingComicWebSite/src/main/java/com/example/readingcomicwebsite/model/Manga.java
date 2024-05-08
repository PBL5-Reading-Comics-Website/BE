package com.example.readingcomicwebsite.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "manga")
@Setter
@Getter
public class Manga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //name
    @Column(name = "name")
    private String name;

    //publishing_company
    @Column(name = "publishing_company")
    private String publishingCompany;

    //author
    @Column(name = "author")
    private String author;

    //artist
    @Column(name = "artist")
    private String artist;

    //cover_image
    @Column(name = "cover_image")
    private String coverImage;

    //status
    @Column(name = "status")
    private String status;

    //reading_status
    @Column(name = "reading_status")
    private String readingStatus;

    //view_number
    @Column(name = "view_number")
    private Integer viewNumber;

    //favourite_number
    @Column(name = "favourite_number")
    private Integer favouriteNumber;

    //comment_number
    @Column(name = "comment_number")
    private Integer commentNumber;

    //publish_at
    @Column(name = "publish_at")
    private String publishAt;

    //update_at
    @Column(name = "update_at")
    private String updateAt;

    //update_user
    @Column(name = "update_user")
    private Integer updateUser;

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @JoinTable(
        name = "manga_tag",
        joinColumns = @JoinColumn(name = "manga_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();
}
