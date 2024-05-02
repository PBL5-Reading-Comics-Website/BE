package com.example.readingcomicwebsite.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "LONGTEXT")
    private String imageUrl;

    @Column(name = "image_id")
    private String imageId;

    @ManyToOne
    @JoinColumn(name = "chapter_id", nullable = false)
    private Chapter chapter;

    public Image(String name, String imageUrl, String imageId, Chapter chapter) {
        this.imageUrl = imageUrl;
        this.imageId = imageId;
        this.chapter = chapter;
    }
}
