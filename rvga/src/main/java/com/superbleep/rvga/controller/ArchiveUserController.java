package com.superbleep.rvga.controller;

import com.superbleep.rvga.exception.*;
import com.superbleep.rvga.model.ArchiveUser;
import com.superbleep.rvga.model.ArchiveUserRole;
import com.superbleep.rvga.model.ArchiveUserUpdate;
import com.superbleep.rvga.service.ArchiveUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class ArchiveUserController {
    private final ArchiveUserService archiveUserService;

    public ArchiveUserController(ArchiveUserService archiveUserService) {
        this.archiveUserService = archiveUserService;
    }

    @PostMapping
    public ResponseEntity<ArchiveUser> create(@Valid @RequestBody ArchiveUser archiveUser) {
        ArchiveUser created = archiveUserService.create(archiveUser);

        return ResponseEntity
                .created(URI.create(STR."/api/users/\{created.getId()}"))
                .body(created);
    }

    @GetMapping
    public List<ArchiveUser> getAll() {
        return archiveUserService.get();
    }

    @GetMapping("/{id}")
    public ArchiveUser getById(@PathVariable Long id) {
        return archiveUserService.getById(id);
    }

    @PatchMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Object> modifyData(
            @PathVariable Long id,
            @Valid @RequestBody ArchiveUserUpdate newUser
    ) {
        ArchiveUser oldUser = archiveUserService.getById(id);

        if(
            newUser.getUsername() == null &&
            newUser.getEmail() == null &&
            newUser.getFirstName() == null &&
            newUser.getLastName() == null
        ) {
            throw new ArchiveUserEmptyBody();
        }

        if(newUser.getUsername() == null) {
            newUser.setUsername(oldUser.getUsername());
        }

        if(newUser.getEmail() == null) {
            newUser.setEmail(oldUser.getEmail());
        }

        if(newUser.getFirstName() == null) {
            newUser.setFirstName(oldUser.getFirstName());
        }

        if(newUser.getLastName() == null) {
            newUser.setLastName(oldUser.getFirstName());
        }

        archiveUserService.modifyData(newUser, oldUser.getId());

        Map<String, String> msg = new HashMap<>();
        msg.put("message", STR."Modified data for user with id \{id}");

        return ResponseEntity.ok(msg);
    }

    @PatchMapping(value = "/pass/{id}", produces = "application/json")
    public ResponseEntity<Object> modifyPassword(
            @PathVariable Long id,
            @NotNull @RequestBody String newPassword
    ) {
        ArchiveUser oldUser = archiveUserService.getById(id);

        if(oldUser.getPassword().equals(newPassword)) {
            throw new ArchiveUserPasswordsIdentical();
        }

        archiveUserService.modifyPassword(newPassword, oldUser.getId());

        Map<String, String> msg = new HashMap<>();
        msg.put("message", STR."Modified password for user with id \{id}");

        return ResponseEntity.ok(msg);
    }

    @PatchMapping(value = "/role/{id}", produces = "application/json")
    public ResponseEntity<Object> modifyRole(
            @PathVariable Long id,
            @NotNull @RequestBody String newRoleString
    ) {
        ArchiveUser oldUser = archiveUserService.getById(id);

        if(oldUser.getRole() != ArchiveUserRole.admin) {
            throw new ArchiveUserRolesForbidden();
        }

        ArchiveUserRole newRole = null;

        for (ArchiveUserRole role : ArchiveUserRole.values()) {
            if (role.name().equals(newRoleString)) {
                newRole = role;
                break;
            }
        }

        if(newRole == null) {
            throw new ArchiveUserRoleNotFound();
        }

        if(oldUser.getRole().equals(newRole)) {
            throw new ArchiveUserRolesIdentical();
        }

        archiveUserService.modifyRole(newRole, oldUser.getId());

        Map<String, String> msg = new HashMap<>();
        msg.put("message", STR."Changed role of user \{id} from \{oldUser.getRole()} to \{newRole}");

        return ResponseEntity.ok(msg);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        archiveUserService.getById(id);
        archiveUserService.delete(id);

        Map<String, String> msg = new HashMap<>();
        msg.put("message", STR."Deleted user with id \{id}");

        return ResponseEntity.ok(msg);
    }
}
