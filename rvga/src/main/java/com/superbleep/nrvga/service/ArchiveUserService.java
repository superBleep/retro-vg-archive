package com.superbleep.nrvga.service;

import com.superbleep.nrvga.exception.ArchiveUserSameEmail;
import com.superbleep.nrvga.exception.ArchiveUserSameUsername;
import com.superbleep.nrvga.exception.ArchiveUserSameUsernameAndEmail;
import com.superbleep.nrvga.model.ArchiveUser;
import com.superbleep.nrvga.repository.ArchiveUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArchiveUserService {
    private final ArchiveUserRepository archiveUserRepository;

    public ArchiveUserService(ArchiveUserRepository archiveUserRepository) {
        this.archiveUserRepository = archiveUserRepository;
    }

    @Transactional
    public ArchiveUser create(ArchiveUser archiveUser) {
        Optional<ArchiveUser> sameUsername = archiveUserRepository.findByUsername(archiveUser.getUsername());
        Optional<ArchiveUser> sameEmail = archiveUserRepository.findByEmail(archiveUser.getEmail());

        if(sameUsername.isPresent())
            if(sameEmail.isPresent())
                throw new ArchiveUserSameUsernameAndEmail(archiveUser.getUsername(), archiveUser.getEmail());
            else
                throw new ArchiveUserSameUsername(archiveUser.getUsername());
        else if(sameEmail.isPresent())
            throw new ArchiveUserSameEmail(archiveUser.getEmail());

        return archiveUserRepository.save(archiveUser);
    }
}
