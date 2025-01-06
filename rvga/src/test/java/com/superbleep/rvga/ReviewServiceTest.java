package com.superbleep.rvga;

import com.superbleep.rvga.dto.EmulatorGet;
import com.superbleep.rvga.dto.GameVersionGet;
import com.superbleep.rvga.dto.ReviewGet;
import com.superbleep.rvga.dto.ReviewPost;
import com.superbleep.rvga.exception.ReviewGameNotOnEmulator;
import com.superbleep.rvga.model.*;
import com.superbleep.rvga.repository.ReviewRepository;
import com.superbleep.rvga.service.ArchiveUserService;
import com.superbleep.rvga.service.EmulatorService;
import com.superbleep.rvga.service.GameVersionService;
import com.superbleep.rvga.service.ReviewService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ArchiveUserService archiveUserService;
    @Mock
    private GameVersionService gameVersionService;
    @Mock
    private EmulatorService emulatorService;

    private static ArchiveUser archiveUser;
    private static GameVersionGet gameVersionGet;
    private static EmulatorGet emulatorGet;
    private static Review review;
    private static ReviewPost reviewPost;
    private static ReviewPost reviewPost2;
    private static ReviewGet reviewGet;

    @BeforeAll
    public static void setUp() {
        archiveUser = new ArchiveUser(1L, new Timestamp(System.currentTimeMillis()), "username",
                "email@mail.com", "1234", "firstName", "lastName",
                ArchiveUserRole.regular);

        Platform platform = new Platform(1L, "name", "manufacturer", new Date());
        Game game = new Game(1L, "title", "developer", "publisher", platform, "genre");
        gameVersionGet = new GameVersionGet("1.0.0", game, new Date(), "notes");

        Emulator emulator = new Emulator(1L, "name", "developer", new Date());
        emulatorGet = new EmulatorGet(emulator, List.of(platform.getId()));

        GameVersion gameVersion = new GameVersion(gameVersionGet);
        review = new Review(1L, archiveUser, gameVersion, new Timestamp(System.currentTimeMillis()),
                emulator, 9, "comment");
        reviewPost = new ReviewPost(1L, 1L, "1.0.0", 1L, 5,
                "comment");
        reviewPost2 = new ReviewPost(1L, 1L, "1.0.0", null, 5,
                "comment");
        reviewGet = new ReviewGet(review);
    }

    @Test
    public void whenAllFound_create_savesReview() {
        // Arrange
        when(archiveUserService.getById(reviewPost.archiveUserId())).thenReturn(archiveUser);
        when(gameVersionService.getById(any())).thenReturn(gameVersionGet);
        when(emulatorService.getById(reviewPost.emulatorId())).thenReturn(emulatorGet);
        when(emulatorService.isGameOnEmulator(emulatorGet.getId(), reviewPost.gameId())).thenReturn(true);
        when(reviewRepository.save(any())).thenReturn(review);

        // Act
        ReviewGet res = reviewService.create(reviewPost);

        // Assert
        assertThat(res).usingRecursiveComparison().isEqualTo(reviewGet);

        verify(archiveUserService).getById(reviewPost.archiveUserId());
        verify(gameVersionService).getById(any());
        verify(emulatorService).getById(reviewPost.emulatorId());
        verify(emulatorService).isGameOnEmulator(emulatorGet.getId(), reviewPost.gameId());
        verify(reviewRepository).save(any());
    }

    @Test
    public void whenGameIsNotOnEmulator_create_throwsReviewGameNotOnEmulator() {
        // Arrange
        when(archiveUserService.getById(reviewPost.archiveUserId())).thenReturn(archiveUser);
        when(gameVersionService.getById(any())).thenReturn(gameVersionGet);
        when(emulatorService.getById(reviewPost.emulatorId())).thenReturn(emulatorGet);
        when(emulatorService.isGameOnEmulator(emulatorGet.getId(), reviewPost.gameId())).thenReturn(false);


        // Act & Assert
        assertThrows(ReviewGameNotOnEmulator.class, () -> reviewService.create(reviewPost));

        verify(archiveUserService).getById(reviewPost.archiveUserId());
        verify(gameVersionService).getById(any());
        verify(emulatorService).getById(reviewPost.emulatorId());
        verify(emulatorService).isGameOnEmulator(emulatorGet.getId(), reviewPost.gameId());
    }

    @Test
    public void whenEmulatorIsNull_create_savesReview() {
        // Arrange
        when(archiveUserService.getById(reviewPost2.archiveUserId())).thenReturn(archiveUser);
        when(gameVersionService.getById(any())).thenReturn(gameVersionGet);
        when(reviewRepository.save(any())).thenReturn(review);

        // Act
        ReviewGet res = reviewService.create(reviewPost2);

        // Assert
        assertThat(res).usingRecursiveComparison().isEqualTo(reviewGet);

        verify(archiveUserService).getById(reviewPost2.archiveUserId());
        verify(gameVersionService).getById(any());
        verify(reviewRepository).save(any());
    }
}
