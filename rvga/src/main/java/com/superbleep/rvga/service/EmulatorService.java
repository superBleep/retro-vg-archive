package com.superbleep.rvga.service;

import com.superbleep.rvga.dto.EmulatorGet;
import com.superbleep.rvga.dto.EmulatorPatch;
import com.superbleep.rvga.dto.EmulatorPost;
import com.superbleep.rvga.exception.EmulatorEmptyBody;
import com.superbleep.rvga.exception.EmulatorEmptyPlatformList;
import com.superbleep.rvga.exception.EmulatorNotFound;
import com.superbleep.rvga.model.Emulator;
import com.superbleep.rvga.model.EmulatorPlatform;
import com.superbleep.rvga.model.Game;
import com.superbleep.rvga.model.Platform;
import com.superbleep.rvga.repository.EmulatorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class EmulatorService {
    private final EmulatorRepository emulatorRepository;
    private final PlatformService platformService;
    private final GameService gameService;
    private final EmulatorPlatformService emulatorPlatformService;

    public EmulatorService(EmulatorRepository emulatorRepository, PlatformService platformService,
                           GameService gameService, EmulatorPlatformService emulatorPlatformService) {
        this.emulatorRepository = emulatorRepository;
        this.platformService = platformService;
        this.gameService = gameService;
        this.emulatorPlatformService = emulatorPlatformService;
    }

    @Transactional
    public Emulator create(EmulatorPost emulatorPost) {
        List<EmulatorPlatform> joins = new ArrayList<>();

        Emulator emulator = new Emulator(emulatorPost);
        Emulator savedEmulator = emulatorRepository.save(emulator);

        for(Long platformId : emulatorPost.platformIds()) {
            Platform platform = platformService.getById(platformId);
            EmulatorPlatform emulatorPlatform = new EmulatorPlatform(savedEmulator, platform);

            joins.add(emulatorPlatform);
        }

        emulatorPlatformService.create(joins);

        return savedEmulator;
    }

    public List<EmulatorGet> getAll() {
        List<Emulator> emulators = emulatorRepository.findAll();
        List<EmulatorGet> res = new ArrayList<>();

        for(Emulator emulator : emulators) {
            List<Long> platformIds = emulatorRepository.findAllPlatformIds(emulator.getId());

            res.add(new EmulatorGet(emulator, platformIds));
        }

        return res;
    }

    public EmulatorGet getById(long id) {
        Optional<Emulator> optional = emulatorRepository.findById(id);

        if(optional.isPresent()) {
            Emulator emulator = optional.get();
            List<Long> platformIds = emulatorRepository.findAllPlatformIds(id);

            return new EmulatorGet(emulator, platformIds);
        } else
            throw new EmulatorNotFound(id);
    }

    public boolean isGameOnEmulator(long emulatorId, long gameId) {
        this.getById(emulatorId);
        gameService.getById(gameId);

        Optional<Game> optional = emulatorRepository.isGameOnEmulator(emulatorId, gameId);

        return optional.isPresent();
    }

    @Transactional
    public void modifyData(EmulatorPatch newEmulator, long id) {
        EmulatorGet oldEmulator = this.getById(id);

        if(newEmulator.getName() == null && newEmulator.getDeveloper() == null && newEmulator.getRelease() == null)
            throw new EmulatorEmptyBody();

        if(newEmulator.getName() == null)
            newEmulator.setName(oldEmulator.getName());

        if(newEmulator.getDeveloper() == null)
            newEmulator.setDeveloper(oldEmulator.getDeveloper());

        if(newEmulator.getRelease() == null)
            newEmulator.setRelease(oldEmulator.getRelease());

        emulatorRepository.modifyData(newEmulator.getName(), newEmulator.getDeveloper(), newEmulator.getRelease(), id);
    }

    @Transactional
    public void modifyPlatforms(List<Long> platformIds, long id) {
        if(platformIds.isEmpty())
            throw new EmulatorEmptyPlatformList();

        EmulatorGet emulatorGet = this.getById(id);
        List<EmulatorPlatform> joins = new ArrayList<>();

        emulatorPlatformService.deleteAllByEmulatorId(id);

        for(Long platformid : platformIds) {
            Platform platform = platformService.getById(platformid);
            Emulator emulator = new Emulator(emulatorGet);

            joins.add(new EmulatorPlatform(emulator, platform));
        }

        emulatorPlatformService.create(joins);
    }

    @Transactional
    public void delete(long id) {
        this.getById(id);

        emulatorRepository.deleteById(id);
    }
}
