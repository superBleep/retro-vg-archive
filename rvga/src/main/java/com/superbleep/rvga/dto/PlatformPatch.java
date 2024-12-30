package com.superbleep.rvga.dto;

import java.util.Date;

public class PlatformPatch {
    private String name;
    private String manufacturer;
    private Date release;

    public PlatformPatch() {
    }

    public PlatformPatch(String name, String manufacturer, Date release) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.release = release;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Date getRelease() {
        return release;
    }

    public void setRelease(Date release) {
        this.release = release;
    }
}
