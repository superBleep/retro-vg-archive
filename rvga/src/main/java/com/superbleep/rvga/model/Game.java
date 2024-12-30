package com.superbleep.rvga.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.superbleep.rvga.dto.GamePost;
import com.superbleep.rvga.util.LazyFieldsFilter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "title")
    @NotBlank
    private String title;
    @Column(name = "developer")
    @NotBlank
    private String developer;
    @Column(name = "publisher")
    @NotBlank
    private String publisher;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "platform_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = LazyFieldsFilter.class)
    private Platform platform;
    @Column(name = "genre")
    @NotBlank
    private String genre;

    public Game() {
    }

    public Game(long id, String title, String developer, String publisher, Platform platform, String genre) {
        this.id = id;
        this.title = title;
        this.developer = developer;
        this.publisher = publisher;
        this.platform = platform;
        this.genre = genre;
    }

    public Game(String title, String developer, String publisher, Platform platform, String genre) {
        this.title = title;
        this.developer = developer;
        this.publisher = publisher;
        this.platform = platform;
        this.genre = genre;
    }

    public Game(GamePost gamePost, Platform platform) {
        this.title = gamePost.title();
        this.developer = gamePost.developer();
        this.publisher = gamePost.publisher();
        this.platform = platform;
        this.genre = gamePost.genre();
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getGenre() {
        return genre;
    }
}
