package com.superbleep.rvga.service;

import com.superbleep.rvga.exception.ArchiveUserNotFound;
import com.superbleep.rvga.exception.PlatformEmptyBody;
import com.superbleep.rvga.exception.PlatformNotFound;
import com.superbleep.rvga.model.ArchiveUser;
import com.superbleep.rvga.model.Platform;
import com.superbleep.rvga.model.PlatformUpdate;
import com.superbleep.rvga.repository.PlatformRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlatformService {
    private final PlatformRepository platformRepository;

    public PlatformService(PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    @Transactional
    public Platform create(Platform platform) {
        return platformRepository.save(platform);
    }

    public List<Platform> getAll() {
        return platformRepository.findAll();
    }

    public Platform getById(long id) {
        Optional<Platform> platformOptional = platformRepository.findById(id);

        if(platformOptional.isPresent()) {
            return platformOptional.get();
        } else {
            throw new PlatformNotFound(id);
        }
    }

    @Transactional
    public void modifyData(PlatformUpdate newPlatform, long id) {
        Platform oldPlatform = this.getById(id);

        if(
            newPlatform.getName() == null &&
            newPlatform.getManufacturer() == null &&
            newPlatform.getRelease() == null
        ) {
            throw new PlatformEmptyBody();
        }

        if(newPlatform.getName() == null) {
            newPlatform.setName(oldPlatform.getName());
        }

        if(newPlatform.getManufacturer() == null) {
            newPlatform.setManufacturer(oldPlatform.getManufacturer());
        }

        if(newPlatform.getRelease() == null) {
            newPlatform.setRelease(oldPlatform.getRelease());
        }

        platformRepository.modifyData(
                newPlatform.getName(),
                newPlatform.getManufacturer(),
                newPlatform.getRelease(),
                id
        );
    }

    @Transactional
    public void delete(long id) {
        this.getById(id);

        platformRepository.deleteById(id);
    }
}
