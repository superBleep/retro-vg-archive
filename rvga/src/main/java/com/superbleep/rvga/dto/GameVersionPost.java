package com.superbleep.rvga.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record GameVersionPost (
        @NotNull String id,
        @NotNull long gameId,
        @NotNull Date release,
        @NotBlank String notes
) {
    public GameVersionPost(@NotNull String id, @NotNull long gameId, @NotNull Date release, @NotBlank String notes) {
        this.id = id;
        this.gameId = gameId;
        this.release = release;
        this.notes = notes;
    }

    @Override
    public @NotNull String id() {
        return id;
    }

    @Override
    public @NotNull long gameId() {
        return gameId;
    }

    @Override
    public @NotNull Date release() {
        return release;
    }

    @Override
    public @NotBlank String notes() {
        return notes;
    }
}

