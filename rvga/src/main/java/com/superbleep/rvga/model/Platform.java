package com.superbleep.rvga.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(name = "platform")
public class Platform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name", unique = true)
    @NotBlank
    private String name;
    @Column(name = "manufacturer")
    @NotBlank
    private String manufacturer;
    @Column(name = "release")
    @NotNull
    private Date release;

    public Platform() {
    }

    public Platform(long id, String name, String manufacturer, Date release) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.release = release;
    }

    public Platform(String name, String manufacturer, Date release) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.release = release;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public Date getRelease() {
        return release;
    }
}
