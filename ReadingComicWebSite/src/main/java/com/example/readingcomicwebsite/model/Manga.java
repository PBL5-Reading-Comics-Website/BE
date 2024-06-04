package com.example.readingcomicwebsite.model;

import com.example.readingcomicwebsite.util.StatusUtil;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.Instant;
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

    //description
    @Column(name = "description")
    private String description;

    //cover_image
    @Column(name = "cover_image")
    private String coverImage;

    //status
    @Column(name = "status", nullable = false)
    private String status = StatusUtil.ONGOING.name();

    //reading_status
    @Column(name = "reading_status", nullable = false)
    private String readingStatus = "READING";

    //view_number
    @Column(name = "view_number", nullable = false)
    private Integer viewNumber = 0;

    //favourite_number
    @Column(name = "favourite_number", nullable = false)
    private Integer favouriteNumber = 0;

    //comment_number
    @Column(name = "comment_number", nullable = false)
    private Integer commentNumber = 0;

    //publish_at
    @Column(name = "publish_at")
    private Instant publishAt;

    //update_at
    @Column(name = "update_at")
    private Instant updateAt;

    //update_user
    @Column(name = "update_user")
    private Integer updateUser;

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @JoinTable(
            name = "manga_tag",
            joinColumns = @JoinColumn(name = "manga_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @JsonBackReference // This annotation is needed for JSON serialization
    private Set<Tag> tags = new HashSet<>();

    //liked user
    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @JoinTable(
            name = "manga_liked_user",
            joinColumns = @JoinColumn(name = "manga_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likedUsers = new HashSet<>();

    @JsonIgnore
    public Set<Tag> getTags() {
        return tags;
    }
}