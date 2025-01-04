package com.superbleep.rvga.model;

import com.superbleep.rvga.dto.GameVersionPost;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Table(name = "game_version")
public class GameVersion {
    @EmbeddedId
    private GameVersionId id;
    @MapsId("gameId")
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "game_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Game game;
    @Column(name = "release")
    @NotNull
    private Date release;
    @Column(name = "notes")
    @NotBlank
    private String notes;

    public GameVersion() {
    }

    public GameVersion(GameVersionId id, Game game, Date release, String notes) {
        this.id = id;
        this.game = game;
        this.release = release;
        this.notes = notes;
    }

    public GameVersion(GameVersionPost gameVersionPost, Game game) {
        this.id = new GameVersionId(gameVersionPost.id(), gameVersionPost.gameId());
        this.game = game;
        this.release = gameVersionPost.release();
        this.notes = gameVersionPost.notes();
    }

    public GameVersionId getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Date getRelease() {
        return release;
    }

    public String getNotes() {
        return notes;
    }
}
