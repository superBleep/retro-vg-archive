package com.superbleep.rvga.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Embeddable
public class EmulatorPlatformId {
    @NotNull
    private long emulatorId;
    @NotNull
    private long platformId;

    public EmulatorPlatformId() {
    }

    public EmulatorPlatformId(long emulatorId, long platformId) {
        this.emulatorId = emulatorId;
        this.platformId = platformId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmulatorPlatformId that)) return false;
        return emulatorId == that.emulatorId && platformId == that.platformId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(emulatorId, platformId);
    }

    public long getEmulatorId() {
        return emulatorId;
    }

    public long getPlatformId() {
        return platformId;
    }
}
