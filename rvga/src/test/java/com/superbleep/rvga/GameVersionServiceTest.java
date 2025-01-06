package com.superbleep.rvga;

import com.superbleep.rvga.dto.GameVersionGet;
import com.superbleep.rvga.dto.GameVersionPatch;
import com.superbleep.rvga.dto.GameVersionPost;
import com.superbleep.rvga.exception.GameNotFound;
import com.superbleep.rvga.exception.GameVersionEmptyBody;
import com.superbleep.rvga.exception.GameVersionNotFound;
import com.superbleep.rvga.exception.GameVersionOnlyOne;
import com.superbleep.rvga.model.Game;
import com.superbleep.rvga.model.GameVersion;
import com.superbleep.rvga.model.GameVersionId;
import com.superbleep.rvga.model.Platform;
import com.superbleep.rvga.repository.GameVersionRepository;
import com.superbleep.rvga.service.GameService;
import com.superbleep.rvga.service.GameVersionService;
import org.junit.jupiter.api.BeforeAll;
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
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    private static GameVersionId gameVersionId;
    private static Game game;
    private static GameVersionPost gameVersionPost;
    private static GameVersionGet gameVersionGet;
    private static Map<String, GameVersionPatch> gameVersionPatches;
    private static GameVersion gameVersion;

    @BeforeAll
    public static void setUp() {
        Platform platform = new Platform(1L, "name", "manufacturer", new Date());

        gameVersionId = new GameVersionId("1.0.0", 1L);
        game = new Game(1L, "title", "developer", "publisher", platform, "genre");
        gameVersionGet = new GameVersionGet("1.0.0", game, new Date(), "notes");
        gameVersionPost = new GameVersionPost("1.0.0", 1L, new Date(), "notes");
        gameVersionPatches = Map.of(
                "VALID", new GameVersionPatch("1.0.0", 1L, new Date(), "notes1"),
                "RELEASE_NULL", new GameVersionPatch("1.0.0", 1L, null, "notes1"),
                "NOTES_NULL", new GameVersionPatch("1.0.0", 1L, new Date(), null),
                "ALL_NULL", new GameVersionPatch("1.0.0", 1L, null, null)
        );
        gameVersion = new GameVersion(gameVersionPost, game);
    }

    @Test
    void whenStandaloneGameVersion_create_savesGameVersion() {
        // Arrange
        when(gameService.getById(gameVersionPost.gameId())).thenReturn(game);
        when(gameVersionRepository.save(any())).thenReturn(gameVersion);

        // Act
        GameVersion res = gameVersionService.create(gameVersionPost, null);

        // Assert
        assertThat(res).isEqualTo(gameVersion);

        verify(gameService).getById(gameVersionPost.gameId());
        verify(gameVersionRepository).save(any());
    }

    @Test
    void whenFirstGameVersion_create_savesGameVersion() {
        // Arrange
        when(gameVersionRepository.save(any())).thenReturn(gameVersion);

        // Act
        GameVersion res = gameVersionService.create(gameVersionPost, game);

        // Assert
        assertThat(res).isEqualTo(gameVersion);

        verify(gameVersionRepository).save(any());
    }

    @Test
    void wehnCalled_getAll_returnsGameVersionList() {
        // Arrange
        when(gameVersionRepository.findAll()).thenReturn(List.of(gameVersion));

        // Act
        List<GameVersionGet> res = gameVersionService.getAll();

        // Assert
        assertThat(res).usingRecursiveComparison().isEqualTo(List.of(gameVersionGet));

        verify(gameVersionRepository).findAll();
    }

    @Test
    void whenGameVersionIsFound_getById_returnsGameVersion() {
        // Arrange
        when(gameVersionRepository.findById(gameVersionId)).thenReturn(Optional.of(gameVersion));

        // Act
        GameVersionGet res = gameVersionService.getById(gameVersionId);

        // Assert
        assertThat(res).usingRecursiveComparison().isEqualTo(gameVersionGet);

        verify(gameVersionRepository).findById(gameVersionId);
    }

    @Test
    void whenGameVersionIsNotFound_getById_returnsGameVersionNotFound() {
        // Arrange
        when(gameVersionRepository.findById(gameVersionId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(GameVersionNotFound.class, () -> gameVersionService.getById(gameVersionId));

        verify(gameVersionRepository).findById(gameVersionId);
    }

    @Test
    void whenGameVersionIsFound_modifyData_modifiesData() {
        // Arrange
        when(gameVersionRepository.findById((GameVersionId) any())).thenReturn(Optional.of(gameVersion));

        // Act
        gameVersionService.modifyData(gameVersionPatches.get("VALID"));

        // Assert
        verify(gameVersionRepository).findById((GameVersionId) any());
        verify(gameVersionRepository).modifyData(any(), any(), any());
    }

    @Test
    void whenGameVersionIsNotFound_modifyData_throwsGameVersionNotFound() {
        // Arrange
        when(gameVersionRepository.findById((GameVersionId) any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(GameVersionNotFound.class,
                () -> gameVersionService.modifyData(gameVersionPatches.get("VALID")));

        verify(gameVersionRepository).findById((GameVersionId) any());
    }

    @Test
    void whenAllFieldsAreNull_modifyData_throwsGameVersionEmptyBody() {
        // Arrange
        when(gameVersionRepository.findById((GameVersionId) any())).thenReturn(Optional.of(gameVersion));

        // Act & Assert
        assertThrows(GameVersionEmptyBody.class,
                () -> gameVersionService.modifyData(gameVersionPatches.get("ALL_NULL")));

        verify(gameVersionRepository).findById((GameVersionId) any());
    }

    @Test
    void whenReleaseIsNull_modifyData_modifiesData() {
        when(gameVersionRepository.findById((GameVersionId) any())).thenReturn(Optional.of(gameVersion));

        // Act
        gameVersionService.modifyData(gameVersionPatches.get("RELEASE_NULL"));

        // Assert
        verify(gameVersionRepository).findById((GameVersionId) any());
        verify(gameVersionRepository).modifyData(any(), any(), any());
    }

    @Test
    void whenNotesIsNull_modifyData_modifiesData() {
        // Arrange
        when(gameVersionRepository.findById((GameVersionId) any())).thenReturn(Optional.of(gameVersion));

        // Act
        gameVersionService.modifyData(gameVersionPatches.get("NOTES_NULL"));

        // Assert
        verify(gameVersionRepository).findById((GameVersionId) any());
        verify(gameVersionRepository).modifyData(any(), any(), any());
    }

    @Test
    void whenGameVersionIsFound_delete_removesGameVersion() {
        when(gameVersionRepository.findById(gameVersionId)).thenReturn(Optional.of(gameVersion));

        // Act
        gameVersionService.delete(gameVersionId);

        // Assert
        verify(gameVersionRepository).findById(gameVersionId);
        verify(gameVersionRepository).deleteById(gameVersionId);
    }

    @Test
    void whenGameVersionIsNotFound_delete_throwsGameVersionNotFound() {
        // Arrange
        when(gameVersionRepository.findById(gameVersionId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(GameVersionNotFound.class, () -> gameVersionService.delete(gameVersionId));

        verify(gameVersionRepository).findById(gameVersionId);
    }

    @Test
    void whenGameVersionIsTheOnlyOne_delete_throwsGameVersionOnlyOne() {
        // Arrange
        when(gameVersionRepository.findById(gameVersionId)).thenReturn(Optional.of(gameVersion));
        when(gameService.getGameVersions(gameVersionId.getGameId())).thenReturn(List.of(gameVersionGet));

        // Act & Assert
        assertThrows(GameVersionOnlyOne.class, () -> gameVersionService.delete(gameVersionId));

        verify(gameVersionRepository).findById(gameVersionId);
        verify(gameService).getGameVersions(gameVersionId.getGameId());
    }
}
