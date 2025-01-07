package com.superbleep.rvga;

import com.superbleep.rvga.dto.*;
import com.superbleep.rvga.exception.ReviewEmptyBody;
import com.superbleep.rvga.exception.ReviewGameNotOnEmulator;
import com.superbleep.rvga.exception.ReviewNotFound;
import com.superbleep.rvga.model.*;
import com.superbleep.rvga.repository.ReviewRepository;
import com.superbleep.rvga.service.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
    @Mock
    private GameService gameService;

    private static Long id;
    private static Long gameId;
    private static ArchiveUser archiveUser;
    private static Game game;
    private static GameVersionGet gameVersionGet;
    private static EmulatorGet emulatorGet;
    private static Review review;
    private static ReviewPost reviewPost;
    private static ReviewPost reviewPost2;
    private static ReviewGet reviewGet;
    private static Map<String, ReviewPatch> reviewPatches;

    @BeforeAll
    public static void setUp() {
        id = 1L;
        gameId = 1L;

        archiveUser = new ArchiveUser(1L, new Timestamp(System.currentTimeMillis()), "username",
                "email@mail.com", "1234", "firstName", "lastName",
                ArchiveUserRole.regular);

        Platform platform = new Platform(1L, "name", "manufacturer", new Date());
        game = new Game(1L, "title", "developer", "publisher", platform, "genre");
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
        reviewPatches = Map.of(
                "VALID", new ReviewPatch(10, "comment"),
                "RATING_NULL", new ReviewPatch(null,  "commnet"),
                "COMMENT_NULL", new ReviewPatch(10, null),
                "ALL_NULL", new ReviewPatch(null, null)
        );
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

    @Test
    public void whenGameIsFound_getAllByGameId_returnsGameList() {
        // Arrange
        when(gameService.getById(gameId)).thenReturn(game);
        when(reviewRepository.findAllByGameId(gameId)).thenReturn(List.of(reviewGet));

        // Act
        List<ReviewGet> res = reviewService.getAllByGameId(gameId);

        // Assert
        assertThat(res).usingRecursiveComparison().isEqualTo(List.of(reviewGet));

        verify(gameService).getById(gameId);
        verify(reviewRepository).findAllByGameId(gameId);
    }

    @Test
    public void whenReviewIsFound_getById_returnsReviewGet() {
        // Arrange
        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));

        // Act
        ReviewGet res = reviewService.getById(id);

        // Assert
        assertThat(res).usingRecursiveComparison().isEqualTo(reviewGet);

        verify(reviewRepository).findById(id);
    }

    @Test
    public void whenReviewIsNotFound_getById_throwsReviewNotFound() {
        // Arrange
        when(reviewRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ReviewNotFound.class, () -> reviewService.getById(id));

        verify(reviewRepository).findById(id);
    }

    @Test
    public void whenReviewIsFound_modifyData_modifiesData() {
        // Arrange
        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));

        // Act
        reviewService.modifyData(reviewPatches.get("VALID"), id);

        // Assert
        verify(reviewRepository).findById(id);
        verify(reviewRepository).modifyData(any(), any(), eq(id));
    }

    @Test
    public void whenReviewIsNotFound_modifyData_throwsReviewNotFound() {
        // Arrange
        when(reviewRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ReviewNotFound.class, () -> reviewService.modifyData(reviewPatches.get("VALID"), id));

        verify(reviewRepository).findById(id);
    }

    @Test
    public void whenAllFieldsAreNull_modifyData_throwsReviewEmptyBody() {
        // Arrange
        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));

        // Act & Assert
        assertThrows(ReviewEmptyBody.class, () -> reviewService.modifyData(reviewPatches.get("ALL_NULL"), id));

        verify(reviewRepository).findById(id);
    }

    @Test
    public void whenRatingIsNull_modifyData_modifiesData() {
        // Arrange
        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));

        // Act
        reviewService.modifyData(reviewPatches.get("RATING_NULL"), id);

        // Assert
        verify(reviewRepository).findById(id);
        verify(reviewRepository).modifyData(any(), any(), eq(id));
    }

    @Test
    public void whenCommentIsNull_modifyData_modifiesData() {
        // Arrange
        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));

        // Act
        reviewService.modifyData(reviewPatches.get("COMMENT_NULL"), id);

        // Assert
        verify(reviewRepository).findById(id);
        verify(reviewRepository).modifyData(any(), any(), eq(id));
    }

    @Test
    public void whenReviewIsFound_delete_removesReview() {
        // Arrange
        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));

        // Act
        reviewService.delete(id);

        // Assert
        verify(reviewRepository).findById(id);
        verify(reviewRepository).deleteById(id);
    }

    @Test
    public void whenReviewIsNotFound_delete_throwsReviewNotFound() {
        // Arrange
        when(reviewRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ReviewNotFound.class, () -> reviewService.delete(id));

        verify(reviewRepository).findById(id);
    }
}
