package com.superbleep.rvga.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record GamePost(
        @NotBlank String title,
        @NotBlank String developer,
        @NotBlank String publisher,
        @NotNull long platformId,
        @NotBlank String genre,
        @NotBlank String initVerId,
        @NotNull Date initVerRelease,
        @NotBlank String initVerNotes
) {
    public GamePost(@NotBlank String title, @NotBlank String developer, @NotBlank String publisher,
                    @NotNull long platformId, @NotBlank String genre, @NotBlank String initVerId,
                    @NotNull Date initVerRelease, @NotBlank String initVerNotes) {
        this.title = title;
        this.developer = developer;
        this.publisher = publisher;
        this.platformId = platformId;
        this.genre = genre;
        this.initVerId = initVerId;
        this.initVerRelease = initVerRelease;
        this.initVerNotes = initVerNotes;
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

    @Override
    public String initVerId() {
        return initVerId;
    }

    @Override
    public Date initVerRelease() {
        return initVerRelease;
    }

    @Override
    public String initVerNotes() {
        return initVerNotes;
    }
}
