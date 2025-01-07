package com.superbleep.rvga.controller;

import com.superbleep.rvga.dto.*;
import com.superbleep.rvga.model.Emulator;
import com.superbleep.rvga.service.EmulatorService;
import com.superbleep.rvga.util.InvalidFieldsResponse;
import com.superbleep.rvga.util.MessageResponse;
import com.superbleep.rvga.util.SqlResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/emulators")
public class EmulatorController {
    private final EmulatorService emulatorService;

    public EmulatorController(EmulatorService emulatorService) {
        this.emulatorService = emulatorService;
    }

    @Operation(summary = "Register a new emulator", description = "Register a new emulator, sent in the request body." +
        "The new emulator must be associated with at least one platform")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Emulator created successfully"),
            @ApiResponse(responseCode = "400", description = "Required fields are missing from the request " +
                    "/ Platform not found",
                    content = @Content(schema = @Schema(implementation = InvalidFieldsResponse.class))),})
    @PostMapping
    public ResponseEntity<Emulator> create(@Valid @RequestBody EmulatorPost emulatorPost) {
        Emulator created = emulatorService.create(emulatorPost);

        return ResponseEntity
                .created(URI.create(STR."/api/emulators/\{created.getId()}"))
                .body(created);
    }

    @Operation(summary = "Get all emulators", description = "Returns all the emulators present in the database, " +
            "as a list. Each emulator also contains the id(s) of its associated platform(s)")
    @ApiResponse(responseCode = "200", description = "Emulators returned succesfully")
    @GetMapping
    public List<EmulatorGet> getAll() {
        return emulatorService.getAll();
    }

    @Operation(summary = "Get emulator by id", description = "Return a specific emulator from the database by its id")
    @Parameter(name = "id", description = "Emulators's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Emulator returned successfully"),
            @ApiResponse(responseCode = "404", description = "Emulator not found",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))})
    @GetMapping("/{id}")
    public EmulatorGet getById(@PathVariable Long id) {
        return emulatorService.getById(id);
    }

    @Operation(summary = "Check game existance", description = "Check if the a game (identified by its id)" +
            " has or doesn't have a platform bound to the supplied emulator (also identified by its id)")
    @Parameters(value = {
            @Parameter(name = "id", description = "Emulator's id"),
            @Parameter(name = "gameId", description = "Game's id")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search successfull"),
            @ApiResponse(responseCode = "404", description = "Emulator / Game not found",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))})
    @GetMapping("/{id}/games/{gameId}")
    public ResponseEntity<Boolean> isGameOnEmulator(@PathVariable Long id, @PathVariable Long gameId) {
        return ResponseEntity.ok(emulatorService.isGameOnEmulator(id, gameId));
    }

    @Operation(summary = "Modify emulator data", description = "Modify an emulators's data. If some of the fields are " +
            "null, they will be replaced with their old values")
    @Parameter(name = "id", description = "Emulator's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Emulator's data modified successfully",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Emulator not found",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Malformed request body / All the fields are null",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "409", description = "There's already an emulator with similar " +
                    "data in the database",
                    content = @Content(schema = @Schema(implementation = SqlResponse.class)))})
    @PatchMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Object> modifyData(@PathVariable Long id, @Valid @RequestBody EmulatorPatch newEmulator) {
        emulatorService.modifyData(newEmulator, id);

        MessageResponse res = new MessageResponse(STR."Modified data for emulator with id \{id}");
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Modify emulator platform(s)", description = "Modify an emulator platform(s). The list" +
            " supplied in the body will replace the current platform mapping for the selected emulator")
    @Parameters(value = {
            @Parameter(name = "id", description = "Emulator's id"),
            @Parameter(name = "platformIds", description = "List of platform ids")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Platforms modifed successfully",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Platform list is empty",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Platform not found",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))})
    @PutMapping(value = "/{id}/platforms", produces = "application/json")
    public ResponseEntity<Object> modifyPlatforms(@PathVariable Long id, @RequestBody List<Long> platformIds) {
        emulatorService.modifyPlatforms(platformIds, id);

        MessageResponse res = new MessageResponse(STR."Modified platforms for emulator with id \{id}");
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Delete an emulator", description = "Remove an emualtor from the database by its id")
    @Parameter(name = "id", description = "Emulator's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Emulator deleted successfully",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Emulator not found",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))})
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        emulatorService.delete(id);

        MessageResponse res = new MessageResponse(STR."Deleted emulator with id \{id}");
        return ResponseEntity.ok(res);
    }
}
