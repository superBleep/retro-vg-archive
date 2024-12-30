package com.superbleep.rvga.service;

import com.superbleep.rvga.exception.*;
import com.superbleep.rvga.model.ArchiveUser;
import com.superbleep.rvga.model.ArchiveUserRole;
import com.superbleep.rvga.dto.ArchiveUserPatch;
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

    public List<ArchiveUser> getAll() {
        return archiveUserRepository.findAll();
    }

    public ArchiveUser getById(long id) {
        Optional<ArchiveUser> archiveUserOptional = archiveUserRepository.findById(id);

        if(archiveUserOptional.isPresent())
            return archiveUserOptional.get();
        else
            throw new ArchiveUserNotFound(id);
    }

    @Transactional
    public void modifyData(ArchiveUserPatch newUser, long id) {
        ArchiveUser oldUser = this.getById(id);

        if(newUser.getUsername() == null && newUser.getEmail() == null && newUser.getFirstName() == null &&
                newUser.getLastName() == null)
            throw new ArchiveUserEmptyBody();

        if(newUser.getUsername() == null)
            newUser.setUsername(oldUser.getUsername());

        if(newUser.getEmail() == null)
            newUser.setEmail(oldUser.getEmail());

        if(newUser.getFirstName() == null)
            newUser.setFirstName(oldUser.getFirstName());

        if(newUser.getLastName() == null)
            newUser.setLastName(oldUser.getFirstName());

        archiveUserRepository.modifyData(newUser.getUsername(), newUser.getEmail(), newUser.getFirstName(),
                newUser.getLastName(), id);
    }

    @Transactional
    public void modifyPassword(String newPassword, long id) {
        ArchiveUser oldUser = this.getById(id);

        if(oldUser.getPassword().equals(newPassword))
            throw new ArchiveUserPasswordsIdentical();

        archiveUserRepository.modifyPassword(newPassword, id);
    }

    @Transactional
    public void modifyRole(String newRoleString, long id) {
        ArchiveUser oldUser = this.getById(id);

        if(oldUser.getRole() != ArchiveUserRole.admin)
            throw new ArchiveUserRolesForbidden();

        ArchiveUserRole newRole = null;

        for (ArchiveUserRole role : ArchiveUserRole.values())
            if (role.name().equals(newRoleString)) {
                newRole = role;
                break;
            }

        if(newRole == null)
            throw new ArchiveUserRoleNotFound();

        if(oldUser.getRole().equals(newRole))
            throw new ArchiveUserRolesIdentical();

        archiveUserRepository.modifyRole(newRole, id);
    }

    @Transactional
    public void delete(long id) {
        this.getById(id);

        archiveUserRepository.deleteById(id);
    }
}
