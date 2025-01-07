package com.superbleep.rvga.controller;

import com.superbleep.rvga.dto.GameGet;
import com.superbleep.rvga.dto.ReviewGet;
import com.superbleep.rvga.dto.ReviewPatch;
import com.superbleep.rvga.dto.ReviewPost;
import com.superbleep.rvga.service.ReviewService;
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
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "Register a new review", description = "Register a new review, sent in the request body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review created successfully"),
            @ApiResponse(responseCode = "400", description = "Required fields are missing from the request / " +
                    "(User / Game version / Emulator) not found / Game isn't available on the emulator",
                    content = @Content(schema = @Schema(implementation = InvalidFieldsResponse.class))),
    })
    @PostMapping
    public ResponseEntity<ReviewGet> create(@Valid @RequestBody ReviewPost reviewPost) {
        ReviewGet created = reviewService.create(reviewPost);

        return ResponseEntity
                .created(URI.create(STR."/api/reviews/\{created.getId()}"))
                .body(created);
    }

    @Operation(summary = "Get reviews by game id", description = "Get all the reviews for a specific game id")
    @Parameter(name = "gameId", description = "Game's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews returned successfully"),
            @ApiResponse(responseCode = "404", description = "Game not found")
    })
    @GetMapping("/games/{gameId}")
    public List<ReviewGet> getAllByGameId(@PathVariable long gameId) {
        return reviewService.getAllByGameId(gameId);
    }

    @Operation(summary = "Get review by id", description = "Return a specific review from the database by its id")
    @Parameter(name = "id", description = "Review's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review returned successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))})
    @GetMapping("/{id}")
    public ReviewGet getById(@PathVariable Long id) {
        return reviewService.getById(id);
    }

    @Operation(summary = "Modify review data", description = "Modify a review's data. If some of the fields are " +
            "null, they will be replaced with their old values")
    @Parameter(name = "id", description = "Review's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review's data modified successfully",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Review not found",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Malformed request body / All the fields are null",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))})
    @PatchMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Object> modifyData(@PathVariable Long id, @Valid @RequestBody ReviewPatch newReview) {
        reviewService.modifyData(newReview, id);

        MessageResponse res = new MessageResponse(STR."Modified data for review with id \{id}");
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Delete a review", description = "Remove a review from the database by its id")
    @Parameter(name = "id", description = "Review's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review deleted successfully",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Review not found",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))})
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        reviewService.delete(id);

        MessageResponse res = new MessageResponse(STR."Deleted review with id \{id}");
        return ResponseEntity.ok(res);
    }
}
