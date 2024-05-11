package com.example.readingcomicwebsite.model;

import com.example.readingcomicwebsite.util.StatusUtil;
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
