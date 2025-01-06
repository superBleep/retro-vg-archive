package com.superbleep.rvga.controller;

import com.superbleep.rvga.dto.ReviewGet;
import com.superbleep.rvga.dto.ReviewPost;
import com.superbleep.rvga.model.Review;
import com.superbleep.rvga.service.ReviewService;
import com.superbleep.rvga.util.InvalidFieldsResponse;
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
}
