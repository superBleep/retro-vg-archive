package com.superbleep.rvga.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public record EmulatorPost(
        @NotBlank String name,
        @NotBlank String developer,
        @NotNull Date release,
        @NotEmpty List<Long> platformIds
) {
    public EmulatorPost(@NotBlank String name, @NotBlank String developer, @NotNull Date release,
                        @NotEmpty List<Long> platformIds) {
        this.name = name;
        this.developer = developer;
        this.release = release;
        this.platformIds = List.copyOf(platformIds);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String developer() {
        return developer;
    }

    @Override
    public Date release() {
        return release;
    }

    @Override
    public List<Long> platformIds() {
        return platformIds;
    }
}
