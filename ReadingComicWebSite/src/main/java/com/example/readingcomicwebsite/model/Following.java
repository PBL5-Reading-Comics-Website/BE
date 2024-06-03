package com.example.readingcomicwebsite.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "following")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Following {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "manga_id")
    private Manga manga;

    public Following(User user, Manga manga) {
        this.user = user;
        this.manga = manga;
    }
}
