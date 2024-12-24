package com.superbleep.rvga.service;

import com.superbleep.rvga.exception.ArchiveUserNotFound;
import com.superbleep.rvga.model.ArchiveUser;
import com.superbleep.rvga.model.ArchiveUserRole;
import com.superbleep.rvga.model.ArchiveUserUpdate;
import com.superbleep.rvga.repository.ArchiveUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArchiveUserService {
    private final ArchiveUserRepository archiveUserRepository;

    public ArchiveUserService(ArchiveUserRepository archiveUserRepository) {
        this.archiveUserRepository = archiveUserRepository;
    }

    @Transactional
    public ArchiveUser create(ArchiveUser archiveUser) {
        return archiveUserRepository.save(archiveUser);
    }

    public List<ArchiveUser> get() {
        return archiveUserRepository.findAll();
    }

    public ArchiveUser getById(long id) {
        Optional<ArchiveUser> archiveUserOptional = archiveUserRepository.findById(id);

        if(archiveUserOptional.isPresent()) {
            return archiveUserOptional.get();
        } else {
            throw new ArchiveUserNotFound(id);
        }
    }

    @Transactional
    public void modifyData(ArchiveUserUpdate archiveUserUpdate, long id) {
        archiveUserRepository.modifyData(
                archiveUserUpdate.getUsername(),
                archiveUserUpdate.getEmail(),
                archiveUserUpdate.getFirstName(),
                archiveUserUpdate.getLastName(),
                id
        );
    }

    @Transactional
    public void modifyPassword(String password, long id) {
        archiveUserRepository.modifyPassword(password, id);
    }

    @Transactional
    public void modifyRole(ArchiveUserRole role, long id) {
        archiveUserRepository.modifyRole(role, id);
    }

    @Transactional
    public void delete(long id) {
        archiveUserRepository.deleteById(id);
    }
}
