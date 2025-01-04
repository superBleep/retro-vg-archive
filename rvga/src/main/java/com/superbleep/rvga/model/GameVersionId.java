package com.superbleep.rvga.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GameVersionId implements Serializable {
    private String id;
    private long gameId;

    public GameVersionId() {
    }

    public GameVersionId(String id, long gameId) {
        this.id = id;
        this.gameId = gameId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameVersionId that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(gameId, that.gameId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gameId);
    }

    public String getId() {
        return id;
    }

    public long getGameId() {
        return gameId;
    }
}
