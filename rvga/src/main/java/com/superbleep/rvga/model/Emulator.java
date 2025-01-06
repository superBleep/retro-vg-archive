package com.superbleep.rvga.model;

import com.superbleep.rvga.dto.EmulatorGet;
import com.superbleep.rvga.dto.EmulatorPost;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(name = "emulator")
public class Emulator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name", unique = true)
    @NotBlank
    private String name;
    @Column(name = "developer")
    @NotBlank
    private String developer;
    @Column(name = "release")
    @NotNull
    private Date release;

    public Emulator() {
    }

    public Emulator(long id, String name, String developer, Date release) {
        this.id = id;
        this.name = name;
        this.developer = developer;
        this.release = release;
    }

    public Emulator(EmulatorPost emulatorPost) {
        this.name = emulatorPost.name();
        this.developer = emulatorPost.developer();
        this.release = emulatorPost.release();
    }

    public Emulator(EmulatorGet emulatorGet) {
        this.id = emulatorGet.getId();
        this.name = emulatorGet.getName();
        this.developer = emulatorGet.getDeveloper();
        this.release = emulatorGet.getRelease();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDeveloper() {
        return developer;
    }

    public Date getRelease() {
        return release;
    }
}
