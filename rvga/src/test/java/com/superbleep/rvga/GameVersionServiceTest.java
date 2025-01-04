package com.superbleep.rvga;

import com.superbleep.rvga.dto.GameVersionGet;
import com.superbleep.rvga.dto.GameVersionPost;
import com.superbleep.rvga.exception.GameNotFound;
import com.superbleep.rvga.model.Game;
import com.superbleep.rvga.model.GameVersion;
import com.superbleep.rvga.model.GameVersionId;
import com.superbleep.rvga.model.Platform;
import com.superbleep.rvga.repository.GameVersionRepository;
import com.superbleep.rvga.service.GameService;
import com.superbleep.rvga.service.GameVersionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GameVersionServiceTest {
    @InjectMocks
    private GameVersionService gameVersionService;

    @Mock
    private GameVersionRepository gameVersionRepository;
    @Mock
    private GameService gameService;

    @Test
    void whenStandaloneGameVersion_create_savesGameVersion() {
        // Arrange
        Platform platform = new Platform(1, "name1", "manufacturer1", new Date());
        Game game = new Game(1, "title1", "developer1", "publisher1", platform, "genre1");
        GameVersionPost gameVersionPost = new GameVersionPost("1.0.0", 1, new Date(), "Initial release");
        GameVersion gameVersion = new GameVersion(gameVersionPost, game);

        when(gameService.getById(1)).thenReturn(game);
        when(gameVersionRepository.save(any())).thenReturn(gameVersion);

        // Act
        GameVersion res = gameVersionService.create(gameVersionPost, null);

        // Assert
        assertThat(res).isEqualTo(gameVersion);

        verify(gameService).getById(1);
        verify(gameVersionRepository).save(any());
    }

    @Test
    void whenFirstGameVersion_create_savesGameVersion() {
        // Arrange
        Platform platform = new Platform(1, "name1", "manufacturer1", new Date());
        Game game = new Game(1, "title1", "developer1", "publisher1", platform, "genre1");
        GameVersionPost gameVersionPost = new GameVersionPost("1.0.0", 1, new Date(), "Initial release");
        GameVersion gameVersion = new GameVersion(gameVersionPost, game);

        when(gameVersionRepository.save(any())).thenReturn(gameVersion);

        // Act
        GameVersion res = gameVersionService.create(gameVersionPost, game);

        // Assert
        assertThat(res).isEqualTo(gameVersion);

        verify(gameVersionRepository).save(any());
    }

    @Test
    void whenGameNotFound_create_throwsGameNotFound() {
        // Arrange
        GameVersionPost gameVersionPost = new GameVersionPost("1.0.0", 1, new Date(), "Initial release");
        String errorMsg = "Game with id 1 doesn't exist in the database";

        when(gameService.getById(1)).thenThrow(new GameNotFound(1));

        // Act // Assert
        Exception exception = assertThrows(GameNotFound.class, () -> gameVersionService.create(gameVersionPost, null));
        assertEquals(exception.getMessage(), errorMsg);

        verify(gameService).getById(1);
    }

    @Test
    void whenGameVersionIdentical_create_throwsDataIntegrityViolation() {
        // Arrange
        GameVersionPost gameVersionPost = new GameVersionPost("1.0.0", 1, new Date(), "Initial release");
        String sqlErrorMsg = "Sql error";

        when(gameVersionRepository.save(any())).thenThrow(new DataIntegrityViolationException("",
                new Throwable(sqlErrorMsg)));

        // Act / Assert
        Exception exception = assertThrows(DataIntegrityViolationException.class,
                () -> gameVersionService.create(gameVersionPost, null));
        assertEquals(exception.getCause().getMessage(), sqlErrorMsg);

        verify(gameVersionRepository).save(any());
    }

    @Test
    void getAll_returnsGameVersionList() {
        // Arrange
        Platform platform = new Platform(1, "name1", "manufacturer1", new Date());
        Game game = new Game(1, "title1", "developer1", "publisher1", platform, "genre1");
        GameVersion gameVersion = new GameVersion(new GameVersionId("1.0.0", game.getId()), game, new Date(), "Release notes");

        when(gameVersionRepository.findAll()).thenReturn(List.of(gameVersion));

        // Act
        List<GameVersionGet> res = gameVersionService.getAll();

        // Assert
        assertThat(res).hasSize(1);

        verify(gameVersionRepository).findAll();
    }
}
