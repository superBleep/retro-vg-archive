package com.superbleep.rvga.controller;

import com.superbleep.rvga.model.ArchiveUser;
import com.superbleep.rvga.service.ArchiveUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class ArchiveUserController {
    private final ArchiveUserService archiveUserService;

    public ArchiveUserController(ArchiveUserService archiveUserService) {
        this.archiveUserService = archiveUserService;
    }

    @GetMapping("/{id}")
    public ArchiveUser getArchiveUser(@PathVariable Long id) {
        return archiveUserService.findById(id);
    }
}
