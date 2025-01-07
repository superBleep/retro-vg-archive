package com.superbleep.rvga.service;

import com.superbleep.rvga.dto.*;
import com.superbleep.rvga.exception.ReviewEmptyBody;
import com.superbleep.rvga.exception.ReviewGameNotOnEmulator;
import com.superbleep.rvga.exception.ReviewNotFound;
import com.superbleep.rvga.model.*;
import com.superbleep.rvga.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ArchiveUserService archiveUserService;
    private final GameVersionService gameVersionService;
    private final EmulatorService emulatorService;
    private final GameService gameService;

    public ReviewService(ReviewRepository reviewRepository, ArchiveUserService archiveUserService,
                         GameVersionService gameVersionService, EmulatorService emulatorService,
                         GameService gameService) {
        this.reviewRepository = reviewRepository;
        this.archiveUserService = archiveUserService;
        this.gameVersionService = gameVersionService;
        this.emulatorService = emulatorService;
        this.gameService = gameService;
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

    public List<ReviewGet> getAllByGameId(long gameId) {
        gameService.getById(gameId);

        return reviewRepository.findAllByGameId(gameId);
    }

    public ReviewGet getById(long id) {
        Optional<Review> optional = reviewRepository.findById(id);

        if(optional.isPresent())
            return new ReviewGet(optional.get());
        else
            throw new ReviewNotFound(id);
    }

    @Transactional
    public void modifyData(ReviewPatch newReview, long id) {
        ReviewGet oldReview = this.getById(id);

        if(newReview.getRating() == null && newReview.getComment() == null)
            throw new ReviewEmptyBody();

        if(newReview.getRating() == null)
            newReview.setRating(oldReview.getRating());

        if(newReview.getComment() == null)
            newReview.setComment(oldReview.getComment());

        reviewRepository.modifyData(newReview.getRating(), newReview.getComment(), id);
    }

    @Transactional
    public void delete(long id) {
        this.getById(id);

        reviewRepository.deleteById(id);
    }
}
