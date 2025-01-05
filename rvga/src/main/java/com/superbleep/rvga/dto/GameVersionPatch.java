package com.superbleep.rvga.dto;

import com.superbleep.rvga.model.GameVersionId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class GameVersionPatch {
    @NotBlank
    private final String id;
    @NotNull
    private final Long gameId;
    private Date release;
    private String notes;

    public GameVersionPatch(String id, Long gameId, Date release, String notes) {
        this.id = id;
        this.gameId = gameId;
        this.release = release;
        this.notes = notes;
    }

    public Date getRelease() {
        return release;
    }

    public void setRelease(Date release) {
        this.release = release;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public Long getGameId() {
        return gameId;
    }
}
