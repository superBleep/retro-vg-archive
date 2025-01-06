package com.superbleep.rvga.service;

import com.superbleep.rvga.model.EmulatorPlatform;
import com.superbleep.rvga.repository.EmulatorPlatformRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmulatorPlatformService {
    private final EmulatorPlatformRepository emulatorPlatformRepository;

    public EmulatorPlatformService(EmulatorPlatformRepository emulatorPlatformRepository) {
        this.emulatorPlatformRepository = emulatorPlatformRepository;
    }

    public void create(List<EmulatorPlatform> joins) {
        emulatorPlatformRepository.saveAll(joins);
    }

    public void deleteAllByEmulatorId(long id) {
        emulatorPlatformRepository.deleteAllByEmulatorId(id);
    }
}