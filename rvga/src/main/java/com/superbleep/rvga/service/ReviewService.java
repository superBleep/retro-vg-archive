package com.superbleep.rvga.service;

import com.superbleep.rvga.dto.EmulatorGet;
import com.superbleep.rvga.dto.GameVersionGet;
import com.superbleep.rvga.dto.ReviewGet;
import com.superbleep.rvga.dto.ReviewPost;
import com.superbleep.rvga.exception.ReviewGameNotOnEmulator;
import com.superbleep.rvga.model.*;
import com.superbleep.rvga.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ArchiveUserService archiveUserService;
    private final GameVersionService gameVersionService;
    private final EmulatorService emulatorService;

    public ReviewService(ReviewRepository reviewRepository, ArchiveUserService archiveUserService,
                         GameVersionService gameVersionService, EmulatorService emulatorService) {
        this.reviewRepository = reviewRepository;
        this.archiveUserService = archiveUserService;
        this.gameVersionService = gameVersionService;
        this.emulatorService = emulatorService;
    }

    @Transactional
    public ReviewGet create(ReviewPost reviewPost) {
        ArchiveUser archiveUser = archiveUserService.getById(reviewPost.archiveUserId());

        GameVersionGet gameVersionGet = gameVersionService.getById(new GameVersionId(reviewPost.gameVersionId(),
                reviewPost.gameId()));
        GameVersion gameVersion = new GameVersion(gameVersionGet);

        if(reviewPost.emulatorId() != null) {
            EmulatorGet emulatorGet = emulatorService.getById(reviewPost.emulatorId());
            Emulator emulator = new Emulator(emulatorGet);

            if(emulatorService.isGameOnEmulator(emulator.getId(), reviewPost.gameId())) {
                Review review = reviewRepository.save(new Review(archiveUser, gameVersion, emulator,
                        reviewPost.rating(), reviewPost.comment()));

                return new ReviewGet(review);
            }
            else
                throw new ReviewGameNotOnEmulator(emulator.getId(), reviewPost.gameId());

        } else {
            Review review = reviewRepository.save(new Review(archiveUser, gameVersion, null,
                    reviewPost.rating(), reviewPost.comment()));

            return new ReviewGet(review);
        }
    }
}
