package com.superbleep.rvga;

import com.superbleep.rvga.dto.GamePatch;
import com.superbleep.rvga.dto.GamePost;
import com.superbleep.rvga.dto.GameVersionGet;
import com.superbleep.rvga.exception.GameEmptyBody;
import com.superbleep.rvga.exception.GameIdenticalFound;
import com.superbleep.rvga.exception.GameNotFound;
import com.superbleep.rvga.model.Game;
import com.superbleep.rvga.model.Platform;
import com.superbleep.rvga.repository.GameRepository;
import com.superbleep.rvga.service.GameService;
import com.superbleep.rvga.service.GameVersionService;
import com.superbleep.rvga.service.PlatformService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GameServiceTest {
    @InjectMocks
    private GameService gameService;

    @Mock
    private GameRepository gameRepository;
    @Mock
    private PlatformService platformService;
    @Mock
    private GameVersionService gameVersionService;

    private static long id;
    private static List<Platform> platforms;
    private static Game savedGame;
    private static Game persistedGame;
    private static GamePost gamePost;
    private static Map<String, GamePatch> gamePatches;
    private static GameVersionGet gameVersionGet;

    @BeforeAll
    public static void setUp() {
        id = 1;
        platforms = List.of(
                new Platform(1L, "name", "manufacturer", new Date()),
                new Platform(2L, "name2", "manufacturer2", new Date())
        );
        persistedGame = new Game("title", "developer", "publisher", platforms.get(1),
                "genre");
        gamePost = new GamePost("title", "developer", "publisher", 1, "genre",
                "1.0.0", new Date(), "notes");
        savedGame = new Game(gamePost, platforms.get(0));
        gamePatches = Map.of(
                "VALID", new GamePatch("title1", "developer1", "publisher1",
                        platforms.get(1).getId(), "genre1"),
                "IDENTICAL", new GamePatch(persistedGame.getTitle(), "developer1", "publisher1",
                        persistedGame.getPlatform().getId(), "genre1"),
                "PLATFORM_NULL", new GamePatch("title1", "developer1", "publisher1",
                        null, "genre1"),
                "REST_NULL", new GamePatch(null, null, null, platforms.get(1).getId(),
                        null),
                "ALL_NULL", new GamePatch(null, null, null, null, null)
        );
        gameVersionGet = new GameVersionGet("1.0.0", savedGame, new Date(), "notes");
    }

    @Test
    void whenGameIsUnique_create_savesGame() {
        // Arrange
        when(platformService.getById(gamePost.platformId())).thenReturn(platforms.getFirst());
        when(gameRepository.findAllByTitleWithPlatform(gamePost.title())).thenReturn(List.of(persistedGame));
        when(gameRepository.save(any())).thenReturn(savedGame);

        // Act
        Game res = gameService.create(gamePost);

        // Assert
        assertThat(res).isEqualTo(savedGame);

        verify(platformService).getById(id);
        verify(gameRepository).findAllByTitleWithPlatform(gamePost.title());
        verify(gameRepository).save(any());
        verify(gameVersionService).create(any(), any());
    }

    @Test
    void whenIdenticalGameIsFound_create_throwsGameIdenticalFound() {
        // Arrange
        when(platformService.getById(gamePost.platformId())).thenReturn(platforms.getFirst());
        when(gameRepository.findAllByTitleWithPlatform(gamePost.title())).thenReturn(List.of(savedGame));

        // Act & Assert
        assertThrows(GameIdenticalFound.class, () -> gameService.create(gamePost));

        verify(platformService).getById(gamePost.platformId());
        verify(gameRepository).findAllByTitleWithPlatform(gamePost.title());
    }

    @Test
    void whenCalled_getAll_returnsAllGames() {
        // Arrange
        when(gameRepository.findAll()).thenReturn(List.of(persistedGame));

        // Act
        List<Game> res = gameService.getAll();

        // Assert
        assertThat(res).usingRecursiveComparison().isEqualTo(List.of(persistedGame));

        verify(gameRepository).findAll();
    }

    @Test
    void whenGameIsFound_getById_returnsGame() {
        // Arrange
        when(gameRepository.findById(id)).thenReturn(Optional.of(persistedGame));

        // Act
        Game res = gameService.getById(id);

        // Assert
        assertThat(res).isEqualTo(persistedGame);

        verify(gameRepository).findById(id);
    }

    @Test
    void whenGameIsNotFound_getById_throwsGameNotFound() {
        // Arrange
        when(gameRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(GameNotFound.class, () -> gameService.getById(id));

        verify(gameRepository).findById(id);
    }

    @Test
    void whenGameIsFound_getByIdFull_returnsGame() {
        // Arrange
        when(gameRepository.findByIdWithPlatform(id)).thenReturn(Optional.of(persistedGame));

        // Act
        Game res = gameService.getByIdFull(id);

        // Assert
        assertThat(res).isEqualTo(persistedGame);

        verify(gameRepository).findByIdWithPlatform(id);
    }

    @Test
    void whenGameIsNotFound_getByIdFull_throwsGameNotFound() {
        // Arrange
        when(gameRepository.findByIdWithPlatform(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(GameNotFound.class, () -> gameService.getByIdFull(id));

        verify(gameRepository).findByIdWithPlatform(id);
    }

    @Test
    void whenGameIsFound_getGameVersions_returnsGameVersions() {
        // Arrange
        when(gameRepository.findById(id)).thenReturn(Optional.of(persistedGame));
        when(gameRepository.findAllGameVersions(id)).thenReturn(List.of(gameVersionGet));

        // Act
        List<GameVersionGet> res = gameService.getGameVersions(id);

        // Assert
        assertThat(res).isEqualTo(List.of(gameVersionGet));

        verify(gameRepository).findById(id);
        verify(gameRepository).findAllGameVersions(id);
    }

    @Test
    void whenGameIsNotFound_getGameVersions_throwsGameNotFound() {
        // Arrange
        when(gameRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(GameNotFound.class, () -> gameService.getGameVersions(id));

        verify(gameRepository).findById(id);
    }

    @Test
    void whenGameIsFound_modifyData_modifiesData() {
        // Arrange
        when(gameRepository.findById(id)).thenReturn(Optional.of(persistedGame));
        when(gameRepository.findAllByTitleWithPlatform(gamePatches.get("VALID").getTitle())).thenReturn(List.of());
        when(platformService.getById(gamePatches.get("VALID").getPlatformId())).thenReturn(platforms.get(1));

        // Act
        gameService.modifyData(gamePatches.get("VALID"), id);

        // Assert
        verify(gameRepository).findById(id);
        verify(gameRepository).findAllByTitleWithPlatform(gamePatches.get("VALID").getTitle());
        verify(platformService).getById(gamePatches.get("VALID").getPlatformId());
        verify(gameRepository).modifyData(any(), any(), any(), any(), any(), eq(id));
    }

    @Test
    void whenGameIsNotFound_modifyData_throwsGameNotFound() {
        // Arrange
        when(gameRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(GameNotFound.class, () -> gameService.modifyData(gamePatches.get("VALID"), id));

        verify(gameRepository).findById(id);
    }

    @Test
    void whenAllFieldsAreNull_modifyData_throwsGameEmptyBody() {
        // Arrange
        when(gameRepository.findById(id)).thenReturn(Optional.of(persistedGame));

        // Act & Assert
        assertThrows(GameEmptyBody.class, () -> gameService.modifyData(gamePatches.get("ALL_NULL"), id));

        verify(gameRepository).findById(id);
    }

    @Test
    void whenIdenticalGameIsFound_modifyData_throwsGameIdenticalFound() {
        // Arrange
        when(gameRepository.findById(id)).thenReturn(Optional.of(persistedGame));
        when(gameRepository.findAllByTitleWithPlatform(gamePatches.get("IDENTICAL").getTitle()))
                .thenReturn(List.of(persistedGame));

        // Act & Assert
        assertThrows(GameIdenticalFound.class, () -> gameService.modifyData(gamePatches.get("IDENTICAL"), id));

        verify(gameRepository).findById(id);
        verify(gameRepository).findAllByTitleWithPlatform(gamePatches.get("IDENTICAL").getTitle());
    }

    @Test
    void whenPlatformIsNull_modifyData_modifiesData() {
        // Arrange
        when(gameRepository.findById(id)).thenReturn(Optional.of(persistedGame));

        // Act
        gameService.modifyData(gamePatches.get("PLATFORM_NULL"), id);

        // Assert
        verify(gameRepository).findById(id);
        verify(gameRepository).modifyData(any(), any(), any(), any(), any(), eq(id));
    }

    @Test
    void whenAllButPlatformAreNull_modifyData_modifiesData() {
        // Arrange
        when(gameRepository.findById(id)).thenReturn(Optional.of(persistedGame));
        when(gameRepository.findAllByTitleWithPlatform(gamePatches.get("REST_NULL").getTitle())).thenReturn(List.of());
        when(platformService.getById(gamePatches.get("REST_NULL").getPlatformId())).thenReturn(platforms.get(1));

        // Act
        gameService.modifyData(gamePatches.get("REST_NULL"), id);

        // Assert
        verify(gameRepository).findById(1L);
        verify(gameRepository).findAllByTitleWithPlatform(gamePatches.get("REST_NULL").getTitle());
        verify(platformService).getById(gamePatches.get("REST_NULL").getPlatformId());
        verify(gameRepository).modifyData(any(), any(), any(), any(), any(), eq(id));
    }

    @Test
    void whenGameIsFound_delete_removesGame() {
        // Arrange
        when(gameRepository.findById(id)).thenReturn(Optional.of(persistedGame));

        // Act
        gameService.delete(id);

        // Assert
        verify(gameRepository).findById(id);
        verify(gameRepository).deleteById(id);
    }

    @Test
    void whenGameIsNotFound_delete_throwsGameNotFound() {
        // Arrange
        when(gameRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(GameNotFound.class, () -> gameService.delete(id));

        verify(gameRepository).findById(id);
    }
}
