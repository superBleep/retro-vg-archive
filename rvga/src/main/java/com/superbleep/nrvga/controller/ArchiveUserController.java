package com.superbleep.nrvga.controller;

import com.superbleep.nrvga.dto.ArchiveUserPost;
import com.superbleep.nrvga.mapper.ArchiveUserMapper;
import com.superbleep.nrvga.model.ArchiveUser;
import com.superbleep.nrvga.service.ArchiveUserService;
import com.superbleep.nrvga.util.FailedValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/archive_users")
public class ArchiveUserController {
    private final ArchiveUserService archiveUserService;
    private final ArchiveUserMapper archiveUserMapper;

    public ArchiveUserController(ArchiveUserService archiveUserService, ArchiveUserMapper archiveUserMapper) {
        this.archiveUserService = archiveUserService;
        this.archiveUserMapper = archiveUserMapper;
    }

    @Operation(summary = "Create a new user",
            description = "Create a new user, with the necesary data sent in the request body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Failed field validation(s)",
                    content = @Content(schema = @Schema(implementation = FailedValidation.class)))})
    @PostMapping
    public ResponseEntity<ArchiveUser> create(@Valid @RequestBody ArchiveUserPost archiveUserPost) {
        ArchiveUser archiveUser = archiveUserMapper.toArchiveUser(archiveUserPost);
        ArchiveUser created = archiveUserService.create(archiveUser);

        return ResponseEntity
                .created(URI.create(STR."/api/users/\{created.getId()}"))
                .body(created);
    }
}
