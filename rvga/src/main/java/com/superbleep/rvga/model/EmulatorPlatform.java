package com.superbleep.rvga.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "emulator_platform")
public class EmulatorPlatform {
    @EmbeddedId
    private EmulatorPlatformId id;
    @MapsId("emulatorId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "emulator_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Emulator emulator;
    @MapsId("platformId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "platform_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Platform platform;

    public EmulatorPlatform() {
    }

    public EmulatorPlatform(Emulator emulator, Platform platform) {
        this.id = new EmulatorPlatformId(emulator.getId(), platform.getId());
        this.emulator = emulator;
        this.platform = platform;
    }
}
