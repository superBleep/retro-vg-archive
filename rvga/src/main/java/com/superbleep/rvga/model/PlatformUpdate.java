package com.superbleep.rvga.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class PlatformUpdate {
    private String name;
    private String manufacturer;
    private Date release;

    public PlatformUpdate() {
    }

    public PlatformUpdate(String name, String manufacturer, Date release) {
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
