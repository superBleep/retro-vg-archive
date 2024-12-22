package com.superbleep.rvga.service;

import com.superbleep.rvga.model.ArchiveUser;
import com.superbleep.rvga.exception.ArchiveUserNotFoundException;
import com.superbleep.rvga.repository.ArchiveUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArchiveUserService {
    private final ArchiveUserRepository archiveUserRepository;

    public ArchiveUserService(ArchiveUserRepository archiveUserRepository) {
        this.archiveUserRepository = archiveUserRepository;
    }

    public ArchiveUser findById(long id) {
        Optional<ArchiveUser> archiveUserOptional = archiveUserRepository.findById(id);

        if(archiveUserOptional.isPresent()) {
            return archiveUserOptional.get();
        } else {
            throw new ArchiveUserNotFoundException(id);
        }
    }
}
