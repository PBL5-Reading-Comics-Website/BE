package com.example.readingcomicwebsite.model;

import com.example.readingcomicwebsite.util.StatusUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "content")
    private String content;

    //status
    @Column(name = "status")
    private StatusUtil status;

    //manga
    @ManyToOne
    @JoinColumn(name = "manga_id")
    private Manga manga;
}
