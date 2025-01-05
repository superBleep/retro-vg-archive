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
        Exception exception = assertThrows(GameNotFound.class, () -> gameVersionService.create(gameVersionPost,
                null));
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
        GameVersion gameVersion = new GameVersion(new GameVersionId("1.0.0", game.getId()), game, new Date(),
                "Release notes");

        when(gameVersionRepository.findAll()).thenReturn(List.of(gameVersion));

        // Act
        List<GameVersionGet> res = gameVersionService.getAll();

        // Assert
        assertThat(res).hasSize(1);

        verify(gameVersionRepository).findAll();
    }

    @Test
    void whenGameVersionFound_getById_returnsGameVersion() {
        // Arrange
        GameVersionId gameVersionId = new GameVersionId("1.0.0", 1);
        Platform platform = new Platform(1, "name1", "manufacturer1", new Date());
        Game game = new Game(1, "title1", "developer1", "publisher1", platform, "genre1");
        GameVersion gameVersion = new GameVersion(new GameVersionId("1.0.0", game.getId()), game, new Date(),
                "Release notes");
        Optional<GameVersion> gameVersionOptional = Optional.of(gameVersion);

        when(gameVersionRepository.findById(gameVersionId)).thenReturn(gameVersionOptional);

        // Act
        GameVersionGet res = gameVersionService.getById(gameVersionId);

        // Assert
        assertThat(res.game()).isEqualTo(game);

        verify(gameVersionRepository).findById(gameVersionId);
    }

    @Test
    void whenGameVersionNotFound_getById_returnsGameVersionNotFound() {
        // Arrange
        GameVersionId gameVersionId = new GameVersionId("1.0.0", 1);
        Optional<GameVersion> gameVersionOptional = Optional.empty();
        String errorMsg = "Game version with id 1.0.0, for game with id 1 doesn't exist in the database";

        when(gameVersionRepository.findById(gameVersionId)).thenReturn(gameVersionOptional);

        // Act / Arrange
        Exception exception = assertThrows(GameVersionNotFound.class, () -> gameVersionService.getById(gameVersionId));
        assertEquals(exception.getMessage(), errorMsg);
        verify(gameVersionRepository).findById(gameVersionId);
    }

    @Test
    void whenBodyIsValid_modifyData_modifiesData() {
        // Arrange
        GameVersionId id = new GameVersionId("1.0.0", 1L);
        Platform platform = new Platform(1, "name", "manufacturer", new Date());
        Game game = new Game(1L, "title", "developer", "publisher", platform, "genre");
        GameVersion oldGameVersion = new GameVersion(id, game, new Date(), "notes");
        Optional<GameVersion> oldGameVersionOptional = Optional.of(oldGameVersion);
        GameVersionPatch newGameVersion = new GameVersionPatch("1.0.0", 1L, new Date(), "new notes");

        when(gameVersionRepository.findById(id)).thenReturn(oldGameVersionOptional);

        // Act
        gameVersionService.modifyData(newGameVersion);

        // Assert
        verify(gameVersionRepository).findById((GameVersionId) any());
        verify(gameVersionRepository).modifyData(eq(newGameVersion.getRelease()), eq(newGameVersion.getNotes()), eq(id));
    }

    @Test
    void whenGameVersionNotFound_modifyData_throwsGameVersionNotFound() {
        // Arrange
        GameVersionId id = new GameVersionId("1.0.0", 1L);
        GameVersionPatch newGameVersion = new GameVersionPatch("1.0.0", 1L, new Date(), "new notes");
        String errorMsg = "Game version with id 1.0.0, for game with id 1 doesn't exist in the database";

        when(gameVersionRepository.findById(id)).thenReturn(Optional.empty());

        // Act / Assert
        Exception exception = assertThrows(GameVersionNotFound.class, () -> gameVersionService.modifyData(newGameVersion));
        assertEquals(exception.getMessage(), errorMsg);

        verify(gameVersionRepository).findById(id);
    }

    @Test
    void whenBodyIsAllNull_modifyData_throwsGameVersionEmptyBody() {
        // Arrange
        GameVersionId id = new GameVersionId("1.0.0", 1L);
        Platform platform = new Platform(1, "name", "manufacturer", new Date());
        Game game = new Game(1L, "title", "developer", "publisher", platform, "genre");
        GameVersion oldGameVersion = new GameVersion(id, game, new Date(), "notes");
        Optional<GameVersion> oldGameVersionOptional = Optional.of(oldGameVersion);
        GameVersionPatch newGameVersion = new GameVersionPatch("1.0.0", 1L, null, null);
        String errorMsg = "Request must modify at least one field";

        when(gameVersionRepository.findById(id)).thenReturn(oldGameVersionOptional);

        // Act / Assert
        Exception exception = assertThrows(GameVersionEmptyBody.class, () -> gameVersionService.modifyData(newGameVersion));
        assertEquals(exception.getMessage(), errorMsg);

        verify(gameVersionRepository).findById(id);
    }

    @Test
    void whenReleaseIsNull_modifyData_modifiesData() {
        // Arrange
        GameVersionId id = new GameVersionId("1.0.0", 1L);
        Platform platform = new Platform(1, "name", "manufacturer", new Date());
        Game game = new Game(1L, "title", "developer", "publisher", platform, "genre");
        GameVersion oldGameVersion = new GameVersion(id, game, new Date(), "notes");
        Optional<GameVersion> oldGameVersionOptional = Optional.of(oldGameVersion);
        GameVersionPatch newGameVersion = new GameVersionPatch("1.0.0", 1L, null, "new notes");

        when(gameVersionRepository.findById(id)).thenReturn(oldGameVersionOptional);

        // Act
        gameVersionService.modifyData(newGameVersion);

        // Assert
        verify(gameVersionRepository).findById((GameVersionId) any());
        verify(gameVersionRepository).modifyData(eq(newGameVersion.getRelease()), eq(newGameVersion.getNotes()), eq(id));
    }

    @Test
    void whenNotesIsNull_modifyData_modifiesData() {
        // Arrange
        GameVersionId id = new GameVersionId("1.0.0", 1L);
        Platform platform = new Platform(1, "name", "manufacturer", new Date());
        Game game = new Game(1L, "title", "developer", "publisher", platform, "genre");
        GameVersion oldGameVersion = new GameVersion(id, game, new Date(), "notes");
        Optional<GameVersion> oldGameVersionOptional = Optional.of(oldGameVersion);
        GameVersionPatch newGameVersion = new GameVersionPatch("1.0.0", 1L, new Date(), null);

        when(gameVersionRepository.findById(id)).thenReturn(oldGameVersionOptional);

        // Act
        gameVersionService.modifyData(newGameVersion);

        // Assert
        verify(gameVersionRepository).findById((GameVersionId) any());
        verify(gameVersionRepository).modifyData(eq(newGameVersion.getRelease()), eq(newGameVersion.getNotes()), eq(id));
    }

    @Test
    void whenGameVersionIsFound_delete_removesGameVersion() {
        // Arrange
        GameVersionId id = new GameVersionId("1.0.0", 1L);
        Platform platform = new Platform(1, "name", "manufacturer", new Date());
        Game game = new Game(1L, "title", "developer", "publisher", platform, "genre");
        GameVersion gameVersion = new GameVersion(id, game, new Date(), "notes");
        Optional<GameVersion> gameVersionOptional = Optional.of(gameVersion);

        when(gameVersionRepository.findById(id)).thenReturn(gameVersionOptional);

        // Act
        gameVersionService.delete(id);

        // Assert
        verify(gameVersionRepository).findById(id);
    }

    @Test
    void whenGameVersionIsNotFound_delete_throwsGameVersionNotFound() {
        // Arrange
        GameVersionId id = new GameVersionId("1.0.0", 1);
        String errorMsg = "Game version with id 1.0.0, for game with id 1 doesn't exist in the database";

        when(gameVersionRepository.findById(id)).thenReturn(Optional.empty());

        // Act / Arrange
        Exception exception = assertThrows(GameVersionNotFound.class, () -> gameVersionService.delete(id));
        assertEquals(exception.getMessage(), errorMsg);
        verify(gameVersionRepository).findById(id);
    }

    @Test
    void whenGameVersionIsTheOnlyOne_delete_throwsGameVersionOnlyOne() {
        // Arrange
        GameVersionId id = new GameVersionId("1.0.0", 1L);
        Platform platform = new Platform(1, "name", "manufacturer", new Date());
        Game game = new Game(1L, "title", "developer", "publisher", platform, "genre");
        GameVersion gameVersion = new GameVersion(id, game, new Date(), "notes");
        GameVersionGet gameVersionGet = new GameVersionGet(id.getId(), game, gameVersion.getRelease(), "notes");
        Optional<GameVersion> gameVersionOptional = Optional.of(gameVersion);
        String errorMsg = "Game version with id 1.0.0 is the only version for game with id 1";

        when(gameVersionRepository.findById(id)).thenReturn(gameVersionOptional);
        when(gameService.getGameVersions(id.getGameId())).thenReturn(List.of(gameVersionGet));

        // Act / Assert
        Exception exception = assertThrows(GameVersionOnlyOne.class, () -> gameVersionService.delete(id));
        assertEquals(exception.getMessage(), errorMsg);

        verify(gameVersionRepository).findById(id);
        verify(gameService).getGameVersions(id.getGameId());
    }
}
