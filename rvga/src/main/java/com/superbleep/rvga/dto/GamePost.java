package com.superbleep.rvga.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GamePost(
        @NotBlank String title,
        @NotBlank String developer,
        @NotBlank String publisher,
        @NotNull long platformId,
        @NotBlank String genre
) {
    public GamePost(String title, String developer, String publisher, long platformId, String genre) {
        this.title = title;
        this.developer = developer;
        this.publisher = publisher;
        this.platformId = platformId;
        this.genre = genre;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public String developer() {
        return developer;
    }

    @Override
    public String publisher() {
        return publisher;
    }

    @Override
    public long platformId() {
        return platformId;
    }

    @Override
    public String genre() { return genre; }
}
