package com.superbleep.rvga.controller;

import com.superbleep.rvga.dto.GameVersionGet;
import com.superbleep.rvga.dto.GameVersionPatch;
import com.superbleep.rvga.dto.GameVersionPost;
import com.superbleep.rvga.model.GameVersion;
import com.superbleep.rvga.model.GameVersionId;
import com.superbleep.rvga.service.GameVersionService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/game_versions")
public class GameVersionController {
    private final GameVersionService gameVersionService;

    public GameVersionController(GameVersionService gameVersionService) {
        this.gameVersionService = gameVersionService;
    }

    @Operation(summary = "Register a new game version", description = "Register a new game version for a game" +
            ", sent in the request body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game version created successfully"),
            @ApiResponse(responseCode = "400", description = "Required fields are missing from the request " +
                    "/ Game not found",
                    content = @Content(schema = @Schema(implementation = InvalidFieldsResponse.class))),
            @ApiResponse(responseCode = "409", description = "Identical game version for the input game found " +
                    "in the databse",
                    content = @Content(schema = @Schema(implementation = SqlResponse.class)))})
    @PostMapping
    public ResponseEntity<GameVersion> create(@Valid @RequestBody GameVersionPost gameVersionPost) {
        GameVersion created = gameVersionService.create(gameVersionPost, null);

        return ResponseEntity
                .created(URI.create(STR."/api/game_versions/\{created.getId()}"))
                .body(created);
    }

    @Operation(summary = "Get all game versions", description = "Returns all the versions for all the games present " +
            "in the database, as a list. The list can be empty if there are no games (therfore, no versions)"
    )
    @ApiResponse(responseCode = "200", description = "Game versions returned successfully")
    @GetMapping
    public List<GameVersionGet> getAll() {
        return gameVersionService.getAll();
    }

    @Operation(summary = "Get game version by id", description = "Return a specific game version from the database " +
            "by its id (both the version id string and the id of the associated game)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game version returned successfully"),
            @ApiResponse(responseCode = "404", description = "Game version not found",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))})
    @GetMapping("/single")
    public GameVersionGet getById(@Valid @RequestBody GameVersionId id) {
        return gameVersionService.getById(id);
    }

    @Operation(summary = "Modify game version data", description = "Modify data for a game version. If some of " +
            "the fields are null, they will be replaced with their old values")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game version's data modified successfully",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Game version not found",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Malformed request body / All the fields are null",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))})
    @PatchMapping(produces = "application/json")
    public ResponseEntity<Object> modifyData(@Valid @RequestBody GameVersionPatch newGameVersion) {
        gameVersionService.modifyData(newGameVersion);

        MessageResponse res = new MessageResponse(STR."Modified data for game version \{newGameVersion.getId()}, " +
                STR."game id \{newGameVersion.getGameId()}");
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Delete a game version", description = "Remove a game version from the database by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game version deleted successfully",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Game version not found",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Game version is the only one for its video game",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))})
    @DeleteMapping(produces = "application/json")
    public ResponseEntity<Object> delete(@RequestBody GameVersionId id) {
        gameVersionService.delete(id);

        MessageResponse res = new MessageResponse(STR."Deleted game version with id \{id.getId()}, " +
                STR."game id \{id.getGameId()}");
        return ResponseEntity.ok(res);
    }
}
