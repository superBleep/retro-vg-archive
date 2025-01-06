package com.superbleep.rvga.dto;

import com.superbleep.rvga.model.Emulator;
import jakarta.persistence.ElementCollection;

import java.util.Date;
import java.util.List;

public class EmulatorGet {
    private Long id;
    private String name;
    private String developer;
    private Date release;
    private List<Long> platformIds;

    public EmulatorGet(Emulator emulator, List<Long> platformIds) {
        this.id = emulator.getId();
        this.name = emulator.getName();
        this.developer = emulator.getDeveloper();
        this.release = emulator.getRelease();
        this.platformIds = platformIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public Date getRelease() {
        return release;
    }

    public void setRelease(Date release) {
        this.release = release;
    }

    public List<Long> getPlatformIds() {
        return platformIds;
    }

    public void setPlatformIds(List<Long> platformIds) {
        this.platformIds = platformIds;
    }
}
