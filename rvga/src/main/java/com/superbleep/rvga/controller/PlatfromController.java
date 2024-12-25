package com.superbleep.rvga.controller;

import com.superbleep.rvga.model.Platform;
import com.superbleep.rvga.model.PlatformUpdate;
import com.superbleep.rvga.service.PlatformService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/platforms")
public class PlatfromController {
    private final PlatformService platformService;

    public PlatfromController(PlatformService platformService) {
        this.platformService = platformService;
    }

    @Operation(
            summary = "Register a new platform",
            description = "Register a new platform, sent in the request body"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Platform created successfully"),
            @ApiResponse(responseCode = "400", description = "Required fields are missing from the request",
                    content = @Content(
                            schema = @Schema(implementation = InvalidFieldsResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<Platform> create(@Valid @RequestBody Platform platform) {
        Platform created = platformService.create(platform);

        return ResponseEntity
                .created(URI.create(STR."/api/platforms/\{created.getId()}"))
                .body(created);
    }

    @Operation(
            summary = "Get all platforms",
            description = "Returns all the platforms present in the database, as a list. The list can be empty if there are no platforms."
    )
    @ApiResponse(responseCode = "200", description = "Platforms returned successfully")
    @GetMapping
    public List<Platform> getAll() {
        return platformService.getAll();
    }

    @Operation(
            summary = "Get platform by id",
            description = "Return a specific platform from the database by its id"
    )
    @Parameter(
            name = "id",
            description = "Platform's id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Platform returned successfully"),
            @ApiResponse(responseCode = "404", description = "Platform not found",
                    content = @Content(
                            schema = @Schema(implementation = MessageResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public Platform getById(@PathVariable Long id) {
        return platformService.getById(id);
    }

    @Operation(
            summary = "Modify platform data",
            description = "Modify a platform's data. If some of the fields are null, they will be replaced with their old values"
    )
    @Parameter(
            name = "id",
            description = "Platform's id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Platform's data modified successfully",
                    content = @Content(
                            schema = @Schema(implementation = MessageResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Platform not found",
                    content = @Content(
                            schema = @Schema(implementation = MessageResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Malformed request body / All the fields are null",
                    content = @Content(
                            schema = @Schema(implementation = MessageResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "409", description = "There's already a platform with similar data in the database",
                    content = @Content(
                            schema = @Schema(implementation = SqlResponse.class)
                    )
            )
    })
    @PatchMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Object> modifyData(
            @PathVariable Long id,
            @Valid @RequestBody PlatformUpdate newPlatform
    ) {
        platformService.modifyData(newPlatform, id);

        MessageResponse res = new MessageResponse(STR."Modified data for platform with id \{id}");
        return ResponseEntity.ok(res);
    }

    @Operation(
            summary = "Delete a platform",
            description = "Remove a platform from the database by its id"
    )
    @Parameter(
            name = "id",
            description = "Platform's id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Platform deleted successfully",
                    content = @Content(
                            schema = @Schema(implementation = MessageResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Platform not found",
                    content = @Content(
                            schema = @Schema(implementation = MessageResponse.class)
                    )
            )
    })
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        platformService.delete(id);

        MessageResponse res = new MessageResponse(STR."Deleted platform with id \{id}");
        return ResponseEntity.ok(res);
    }
}
