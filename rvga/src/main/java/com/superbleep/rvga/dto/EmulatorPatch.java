package com.superbleep.rvga.dto;

import java.util.Date;

public class EmulatorPatch {
    private String name;
    private String developer;
    private Date release;

    public EmulatorPatch(String name, String developer, Date release) {
        this.name = name;
        this.developer = developer;
        this.release = release;
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
}
