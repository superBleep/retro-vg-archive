package com.superbleep.rvga.model;

import com.superbleep.rvga.dto.ReviewPost;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import java.sql.Timestamp;

@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ArchiveUser archiveUser;
    @ManyToOne(optional = false)
    @JoinColumns({
            @JoinColumn(name = "version_id", referencedColumnName = "id"),
            @JoinColumn(name = "game_id", referencedColumnName = "game_id")
    })
    private GameVersion gameVersion;
    @Column(name = "creation_date")
    @CreationTimestamp
    private Timestamp creationDate;
    @ManyToOne
    @JoinColumn(name = "emulator_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Emulator emulator;
    @Column(name = "rating")
    @Range(min = 0, max = 10)
    @NotNull
    private int rating;
    @Column(name = "comment")
    @NotBlank
    private String comment;

    public Review() {
    }

    public Review(long id, ArchiveUser archiveUser, GameVersion gameVersion, Timestamp creationDate, Emulator emulator,
                  int rating, String comment) {
        this.id = id;
        this.archiveUser = archiveUser;
        this.gameVersion = gameVersion;
        this.creationDate = creationDate;
        this.emulator = emulator;
        this.rating = rating;
        this.comment = comment;
    }

    public Review(ArchiveUser archiveUser, GameVersion gameVersion, Emulator emulator, int rating, String comment) {
        this.archiveUser = archiveUser;
        this.gameVersion = gameVersion;
        this.emulator = emulator;
        this.rating = rating;
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public ArchiveUser getArchiveUser() {
        return archiveUser;
    }

    public GameVersion getGameVersion() {
        return gameVersion;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public Emulator getEmulator() {
        return emulator;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }
}
