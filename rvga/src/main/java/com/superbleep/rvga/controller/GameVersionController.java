package com.superbleep.rvga.controller;

import com.superbleep.rvga.dto.GameVersionGet;
import com.superbleep.rvga.dto.GameVersionPost;
import com.superbleep.rvga.model.GameVersion;
import com.superbleep.rvga.service.GameVersionService;
import com.superbleep.rvga.util.InvalidFieldsResponse;
import com.superbleep.rvga.util.SqlResponse;
import io.swagger.v3.oas.annotations.Operation;
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
}
