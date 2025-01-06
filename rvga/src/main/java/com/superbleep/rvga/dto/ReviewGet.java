package com.superbleep.rvga.dto;

import com.superbleep.rvga.model.Review;

import java.sql.Timestamp;

public class ReviewGet {
    private final long id;
    private final long archiveUserId;
    private final long gameId;
    private final String versionId;
    private final Timestamp creationDate;
    private final Long emulatorId;
    private final int rating;
    private final String comment;

    public ReviewGet(long id, long archiveUserId, long gameId, String versionId, Timestamp creationDate,
                     long emulatorId, int rating, String comment) {
        this.id = id;
        this.archiveUserId = archiveUserId;
        this.gameId = gameId;
        this.versionId = versionId;
        this.creationDate = creationDate;
        this.emulatorId = emulatorId;
        this.rating = rating;
        this.comment = comment;
    }

    public ReviewGet(Review review) {
        id = review.getId();
        archiveUserId = review.getArchiveUser().getId();
        gameId = review.getGameVersion().getId().getGameId();
        versionId = review.getGameVersion().getId().getId();
        creationDate = review.getCreationDate();

        if(review.getEmulator() != null)
            emulatorId = review.getEmulator().getId();
        else
            emulatorId = null;

        rating = review.getRating();
        comment = review.getComment();
    }

    public long getId() {
        return id;
    }

    public long getArchiveUserId() {
        return archiveUserId;
    }

    public long getGameId() {
        return gameId;
    }

    public String getVersionId() {
        return versionId;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public Long getEmulatorId() {
        return emulatorId;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }
}
