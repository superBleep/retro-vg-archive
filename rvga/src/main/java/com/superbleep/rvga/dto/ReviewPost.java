package com.superbleep.rvga.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewPost (
        @NotNull Long archiveUserId,
        @NotNull Long gameId,
        @NotBlank String gameVersionId,
        Long emulatorId,
        @NotNull Integer rating,
        @NotBlank String comment
) {
    public ReviewPost(@NotNull Long archiveUserId, @NotNull Long gameId, @NotBlank String gameVersionId,
                      Long emulatorId, @NotNull Integer rating, @NotBlank String comment) {
        this.archiveUserId = archiveUserId;
        this.gameId = gameId;
        this.gameVersionId = gameVersionId;
        this.emulatorId = emulatorId;
        this.rating = rating;
        this.comment = comment;
    }

    @Override
    public Long archiveUserId() {
        return archiveUserId;
    }

    @Override
    public Long gameId() {
        return gameId;
    }

    public String gameVersionId() {
        return gameVersionId;
    }

    @Override
    public Long emulatorId() {
        return emulatorId;
    }

    @Override
    public Integer rating() {
        return rating;
    }

    @Override
    public String comment() {
        return comment;
    }
}
