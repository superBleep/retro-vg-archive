package com.superbleep.rvga.controller;

import com.superbleep.rvga.dto.GamePatch;
import com.superbleep.rvga.dto.GamePost;
import com.superbleep.rvga.model.Game;
import com.superbleep.rvga.model.Platform;
import com.superbleep.rvga.model.PlatformUpdate;
import com.superbleep.rvga.service.GameService;
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
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @Operation(
            summary = "Register a new game",
            description = "Register a new game, sent in the request body"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game created successfully"),
            @ApiResponse(responseCode = "400", description = "Required fields are missing from the request / Platform not found",
                    content = @Content(
                            schema = @Schema(implementation = InvalidFieldsResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "409", description = "Identical game for the input platform found in the databse",
                    content = @Content(
                            schema = @Schema(implementation = SqlResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<Game> create(@Valid @RequestBody GamePost gamePost) {
        Game created = gameService.create(gamePost);

        return ResponseEntity
                .created(URI.create(STR."/api/games/\{created.getId()}"))
                .body(created);
    }

    @Operation(
            summary = "Get all games",
            description = "Returns all the games present in the database, as a list. The list can be empty if there are no games. Platform data not included."
    )
    @ApiResponse(responseCode = "200", description = "Games returned successfully")
    @GetMapping
    public List<Game> getAll() {
        return gameService.getAll();
    }

    @Operation(
            summary = "Get game by id",
            description = "Return a specific game from the database by its id. Platform data inclusion optional"
    )
    @Parameters(value = {
            @Parameter(name = "id", description = "Game's id"),
            @Parameter(name = "full", description = "Include platform data (boolean)")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game returned successfully"),
            @ApiResponse(responseCode = "404", description = "Game not found",
                    content = @Content(
                            schema = @Schema(implementation = MessageResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public Game getById(@PathVariable Long id, @RequestParam(defaultValue = "false") Boolean full) {
        if (full)
            return gameService.getByIdFull(id);
        else
            return gameService.getById(id);
    }

    @Operation(
            summary = "Modify game data",
            description = "Modify a game's data. If some of the fields are null, they will be replaced with their old values"
    )
    @Parameter(
            name = "id",
            description = "Game's id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game's data modified successfully",
                    content = @Content(
                            schema = @Schema(implementation = MessageResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Game not found",
                    content = @Content(
                            schema = @Schema(implementation = MessageResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Malformed request body / All the fields are null",
                    content = @Content(
                            schema = @Schema(implementation = MessageResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "409", description = "There's already a game with similar data in the database",
                    content = @Content(
                            schema = @Schema(implementation = SqlResponse.class)
                    )
            )
    })
    @PatchMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Object> modifyData(
            @PathVariable Long id,
            @Valid @RequestBody GamePatch newGame
    ) {
        gameService.modifyData(newGame, id);

        MessageResponse res = new MessageResponse(STR."Modified data for game with id \{id}");
        return ResponseEntity.ok(res);
    }

    @Operation(
            summary = "Delete a game",
            description = "Remove a game from the database by its id"
    )
    @Parameter(
            name = "id",
            description = "Game's id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game deleted successfully",
                    content = @Content(
                            schema = @Schema(implementation = MessageResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Game not found",
                    content = @Content(
                            schema = @Schema(implementation = MessageResponse.class)
                    )
            )
    })
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        gameService.delete(id);

        MessageResponse res = new MessageResponse(STR."Deleted game with id \{id}");
        return ResponseEntity.ok(res);
    }
}
