package com.superbleep.rvga.controller;

import com.superbleep.rvga.model.ArchiveUser;
import com.superbleep.rvga.model.ArchiveUserRole;
import com.superbleep.rvga.dto.ArchiveUserPatch;
import com.superbleep.rvga.service.ArchiveUserService;
import com.superbleep.rvga.util.InvalidFieldsResponse;
import com.superbleep.rvga.util.MessageResponse;
import com.superbleep.rvga.util.SqlResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ArchiveUserController {
    private final ArchiveUserService archiveUserService;

    public ArchiveUserController(ArchiveUserService archiveUserService) {
        this.archiveUserService = archiveUserService;
    }

    @Operation(summary = "Register a new user", description = "Register a new user, sent in the request body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Required fields are missing from the request",
                    content = @Content(schema = @Schema(implementation = InvalidFieldsResponse.class)))})
    @PostMapping
    public ResponseEntity<ArchiveUser> create(@Valid @RequestBody ArchiveUser archiveUser) {
        ArchiveUser created = archiveUserService.create(archiveUser);

        return ResponseEntity
                .created(URI.create(STR."/api/users/\{created.getId()}"))
                .body(created);
    }

    @Operation(summary = "Get all users", description = "Returns all the users present in the database, as a list. " +
            "The list can be empty if there are no users.")
    @ApiResponse(responseCode = "200", description = "Users returned successfully")
    @GetMapping
    public List<ArchiveUser> getAll() {
        return archiveUserService.getAll();
    }

    @Operation(summary = "Get user by id", description = "Return a specific user from the database by its id")
    @Parameter(name = "id", description = "User's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User returned successfully"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))})
    @GetMapping("/{id}")
    public ArchiveUser getById(@PathVariable Long id) {
        return archiveUserService.getById(id);
    }

    @Operation(summary = "Modify user data", description = "Modify a user's data, except its password, role, and " +
            "generated fields. If some of the fields are null, they will be replaced with their old values")
    @Parameter(name = "id", description = "User's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's data modified successfully",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Malformed request body / All the fields are null",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "409", description = "There's already a user with similar data in the database",
                    content = @Content(schema = @Schema(implementation = SqlResponse.class)))})
    @PatchMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Object> modifyData(@PathVariable Long id, @Valid @RequestBody ArchiveUserPatch newUser) {
        archiveUserService.modifyData(newUser, id);

        MessageResponse res = new MessageResponse(STR."Modified data for user with id \{id}");
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Modify user password", description = "Modify a user's password")
    @Parameter(name = "id", description = "User's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's password modified successfully",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Malformed request body / New password is the same as the old password",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))})
    @PatchMapping(value = "/pass/{id}", produces = "application/json")
    public ResponseEntity<Object> modifyPassword(@PathVariable Long id, @NotNull @RequestBody String newPassword) {
        archiveUserService.modifyPassword(newPassword, id);

        MessageResponse res = new MessageResponse(STR."Modified password for user with id \{id}");
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Modify user role", description = "Modify a user's role")
    @Parameter(name = "id", description = "User's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's role modified successfully",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Malformed request body / User role not found",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "403", description = "Only admins can modify user roles",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))})
    @PatchMapping(value = "/role/{id}", produces = "application/json")
    public ResponseEntity<Object> modifyRole(@PathVariable Long id, @NotNull @RequestBody String newRoleString) {
        archiveUserService.modifyRole(newRoleString, id);

        ArchiveUserRole oldRole = archiveUserService.getById(id).getRole();

        MessageResponse res = new MessageResponse(STR."Changed role of user \{id} from \{oldRole} to \{newRoleString}");
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Delete a user", description = "Remove a user from the database by its id")
    @Parameter(name = "id", description = "User's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))})
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        archiveUserService.delete(id);

        MessageResponse res = new MessageResponse(STR."Deleted user with id \{id}");
        return ResponseEntity.ok(res);
    }
}
