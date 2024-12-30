package com.superbleep.rvga;

import com.superbleep.rvga.dto.GamePatch;
import com.superbleep.rvga.dto.GamePost;
import com.superbleep.rvga.exception.*;
import com.superbleep.rvga.model.Game;
import com.superbleep.rvga.model.Platform;
import com.superbleep.rvga.repository.GameRepository;
import com.superbleep.rvga.service.GameService;
import com.superbleep.rvga.service.PlatformService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

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
public class GameServiceTest {
    @InjectMocks
    private GameService gameService;

    @Mock
    private GameRepository gameRepository;
    @Mock
    private PlatformService platformService;

    @Test
    void whenBodyIsValid_create_savesGame() {
        // Arrange
        Platform platform1 = new Platform(1, "testName1", "testManufacturer1", new Date());
        Platform platform2 = new Platform(2, "testName2", "testManufacturer2", new Date());
        GamePost gamePost = new GamePost("testTitle", "testDeveloper", "testPublisher",
                1, "testGenre");
        Game persistedGame = new Game(1, "testTitle", "testDeveloper7", "testPublisher7",
                platform2, "testGenre7");
        Game savedGame = new Game(1, "testTitle", "testDeveloper", "testPublisher",
                platform1, "testGenre");

        when(platformService.getById(gamePost.platformId())).thenReturn(platform1);
        when(gameRepository.findAllByTitleWithPlatform(gamePost.title())).thenReturn(List.of(persistedGame));
        when(gameRepository.save(any())).thenReturn(savedGame);

        // Act
        Game res = gameService.create(gamePost);

        // Assert
        assertThat(res).isEqualTo(savedGame);

        verify(platformService).getById(1L);
        verify(gameRepository).findAllByTitleWithPlatform(gamePost.title());
        verify(gameRepository).save(any());
    }

    @Test
    void whenPlatformNotFound_create_throwsPlatformNotFound() {
        // Arrange
        GamePost gamePost = new GamePost("testTitle", "testDeveloper", "testPublisher",
                1L, "testGenre");
        String errorMsg = "Platform with id 1 doesn't exist in the database";

        when(platformService.getById(gamePost.platformId())).thenThrow(new PlatformNotFound(1));

        // Act / Assert
        Exception exception = assertThrows(PlatformNotFound.class, () -> gameService.create(gamePost));
        assertEquals(exception.getMessage(), errorMsg);

        verify(platformService).getById(1);
    }

    @Test
    void whenIdenticalGame_create_throwsGameIdenticalFound() {
        // Arrange
        Platform platform = new Platform(1, "testName1", "testManufacturer1", new Date());
        GamePost gamePost = new GamePost("testTitle", "testDeveloper", "testPublisher",
                1L, "testGenre");
        Game persistedGame = new Game(1, "testTitle", "testDeveloper7", "testPublisher7",
                platform, "testGenre7");
        String errorMsg = "There's already a game with the same title, released on the same platform.";

        when(platformService.getById(gamePost.platformId())).thenReturn(platform);
        when(gameRepository.findAllByTitleWithPlatform(gamePost.title())).thenReturn(List.of(persistedGame));

        // Act / Assert
        Exception exception = assertThrows(GameIdenticalFound.class, () -> gameService.create(gamePost));
        assertEquals(exception.getMessage(), errorMsg);

        verify(platformService).getById(gamePost.platformId());
        verify(gameRepository).findAllByTitleWithPlatform(gamePost.title());
    }

    @Test
    void getAll_returnsAllGames() {
        // Arrange
        Platform platform = new Platform(1, "testName", "testManufacturer", new Date());
        Game persistedGame1 = new Game(1, "testTitle1", "testDeveloper", "testPublisher",
                platform, "testGenre");
        Game persistedGame2 = new Game(2, "testTitle2", "testDeveloper", "testPublisher",
                platform, "testGenre");

        when(gameRepository.findAll()).thenReturn(List.of(persistedGame1, persistedGame2));

        // Act
        List<Game> res = gameService.getAll();

        // Assert
        assertThat(res).hasSize(2);

        verify(gameRepository).findAll();
    }

    @Test
    void whenGameFound_getById_returnsGame() {
        // Arrange
        Game persistedGame = new Game(1, "testTitle", "testDeveloper", "testPublisher",
                null, "testGenre");
        Optional<Game> gameOptional = Optional.of(persistedGame);

        when(gameRepository.findById(1L)).thenReturn(gameOptional);

        // Act
        Game res = gameService.getById(1);

        // Assert
        assertThat(res).isEqualTo(persistedGame);

        verify(gameRepository).findById(1L);
    }

    @Test
    void whenGameNotFound_getById_throwsGameNotFound() {
        // Arrange
        String errorMsg = "Game with id 1 doesn't exist in the database";
        Optional<Game> gameOptional = Optional.empty();

        when(gameRepository.findById(1L)).thenReturn(gameOptional);

        // Act / Assert
        Exception exception = assertThrows(GameNotFound.class, () -> gameService.getById(1));
        assertEquals(exception.getMessage(), errorMsg);
        verify(gameRepository).findById(1L);
    }

    @Test
    void whenGameFound_getByIdFull_returnsGame() {
        // Arrange
        Platform platform = new Platform(1, "testName", "testManufacturer", new Date());
        Game persistedGame = new Game(1, "testTitle", "testDeveloper", "testPublisher",
                platform, "testGenre");
        Optional<Game> gameOptional = Optional.of(persistedGame);

        when(gameRepository.findByIdWithPlatform(1L)).thenReturn(gameOptional);

        // Act
        Game res = gameService.getByIdFull(1);

        // Assert
        assertThat(res).isEqualTo(persistedGame);

        verify(gameRepository).findByIdWithPlatform(1L);
    }

    @Test
    void whenGameNotFound_getByIdFull_throwsGameNotFound() {
        // Arrange
        String errorMsg = "Game with id 1 doesn't exist in the database";
        Optional<Game> gameOptional = Optional.empty();

        when(gameRepository.findByIdWithPlatform(1L)).thenReturn(gameOptional);

        // Act / Assert
        Exception exception = assertThrows(GameNotFound.class, () -> gameService.getByIdFull(1));
        assertEquals(exception.getMessage(), errorMsg);
        verify(gameRepository).findByIdWithPlatform(1L);
    }

    @Test
    void whenBodyIsValid_modifyData_modifiesData() {
        // Arrange
        Platform platform1 = new Platform(1, "testName", "testManufacturer", new Date());
        Platform platform2 = new Platform(2, "testName2", "testManufacturer2", new Date());
        Game oldGame = new Game(1, "testTitle", "testDeveloper", "testPublisher",
                platform1, "testGenre");
        GamePatch newGame = new GamePatch("testTitle1", "testDeveloper1", "testPublisher1",
                2L, "testGenre1");
        Optional<Game> oldGameOptional = Optional.of(oldGame);

        when(gameRepository.findById(1L)).thenReturn(oldGameOptional);
        when(gameRepository.findAllByTitleWithPlatform(newGame.getTitle())).thenReturn(List.of());
        when(platformService.getById(newGame.getPlatformId())).thenReturn(platform2);

        // Act
        gameService.modifyData(newGame, 1);

        // Assert
        verify(gameRepository).findById(1L);
        verify(gameRepository).findAllByTitleWithPlatform(newGame.getTitle());
        verify(platformService).getById(newGame.getPlatformId());
        verify(gameRepository).modifyData(eq(newGame.getTitle()), eq(newGame.getDeveloper()), eq(newGame.getPublisher()),
                any(), eq(newGame.getGenre()), eq(1L));
    }

    @Test
    void whenGameNotFound_modifyData_throwsGameNotFound() {
        // Arrange
        GamePatch newGame = new GamePatch("testTitle1", "testDeveloper1", "testPublisher1",
                2L, "testGenre1");
        Optional<Game> oldGameOptional = Optional.empty();
        String errorMsg = "Game with id 1 doesn't exist in the database";

        when(gameRepository.findById(1L)).thenReturn(oldGameOptional);

        // Act / Assert
        Exception exception = assertThrows(GameNotFound.class, () -> gameService.modifyData(newGame, 1));
        assertEquals(exception.getMessage(), errorMsg);
        verify(gameRepository).findById(1L);
    }

    @Test
    void whenBodyIsAllNull_modifyData_throwsGameEmptyBody() {
        // Arrange
        Platform platform = new Platform(1, "testName", "testManufacturer", new Date());
        Game oldGame = new Game(1, "testTitle", "testDeveloper", "testPublisher",
                platform, "testGenre");
        GamePatch newGame = new GamePatch(null, null, null, null, null);
        Optional<Game> oldGameOptional = Optional.of(oldGame);
        String errorMsg = "Request must modify at least one field";

        when(gameRepository.findById(1L)).thenReturn(oldGameOptional);

        // Act / Assert
        Exception exception = assertThrows(GameEmptyBody.class, () -> gameService.modifyData(newGame, 1));
        assertEquals(exception.getMessage(), errorMsg);
        verify(gameRepository).findById(1L);
    }

    @Test
    void whenIdenticalGameFound_modifyData_throwsGameIdenticalFound() {
        // Arrange
        Platform platform = new Platform(1, "testName", "testManufacturer", new Date());
        Game oldGame = new Game(1, "testTitle1", "testDeveloper", "testPublisher",
                platform, "testGenre");
        Game persistedGame = new Game(1, "testTitle2", "testDeveloper", "testPublisher",
                platform, "testGenre");
        GamePatch newGame = new GamePatch("testTitle2", "testDeveloper1", "testPublisher1",
                platform.getId(), "testGenre1");
        Optional<Game> oldGameOptional = Optional.of(oldGame);
        String errorMsg = "There's already a game with the same title, released on the same platform.";

        when(gameRepository.findById(1L)).thenReturn(oldGameOptional);
        when(gameRepository.findAllByTitleWithPlatform(newGame.getTitle())).thenReturn(List.of(persistedGame));

        // Act / Assert
        Exception exception = assertThrows(GameIdenticalFound.class, () -> gameService.modifyData(newGame, 1));
        assertEquals(exception.getMessage(), errorMsg);
        verify(gameRepository).findById(1L);
        verify(gameRepository).findAllByTitleWithPlatform(newGame.getTitle());
    }

    @Test
    void whenPlatformIsNull_modifyData_modifiesData() {
        // Arrange
        Platform platform1 = new Platform(1, "testName", "testManufacturer", new Date());
        Game oldGame = new Game(1, "testTitle", "testDeveloper", "testPublisher",
                platform1, "testGenre");
        GamePatch newGame = new GamePatch("testTitle1", "testDeveloper1", "testPublisher1",
                null, "testGenre1");
        Optional<Game> oldGameOptional = Optional.of(oldGame);

        when(gameRepository.findById(1L)).thenReturn(oldGameOptional);

        // Act
        gameService.modifyData(newGame, 1);

        // Assert
        verify(gameRepository).findById(1L);
        verify(gameRepository).modifyData(eq(newGame.getTitle()), eq(newGame.getDeveloper()), eq(newGame.getPublisher()),
                any(), eq(newGame.getGenre()), eq(1L));
    }

    @Test
    void whenAllButPlatformIsNull_modifyData_modifiesData() {
        // Arrange
        Platform platform1 = new Platform(1, "testName", "testManufacturer", new Date());
        Platform platform2 = new Platform(2, "testName2", "testManufacturer2", new Date());
        Game oldGame = new Game(1, "testTitle", "testDeveloper", "testPublisher",
                platform1, "testGenre");
        GamePatch newGame = new GamePatch(null, null, null,
                2L, null);
        Optional<Game> oldGameOptional = Optional.of(oldGame);

        when(gameRepository.findById(1L)).thenReturn(oldGameOptional);
        when(gameRepository.findAllByTitleWithPlatform(newGame.getTitle())).thenReturn(List.of());
        when(platformService.getById(newGame.getPlatformId())).thenReturn(platform2);

        // Act
        gameService.modifyData(newGame, 1);

        // Assert
        verify(gameRepository).findById(1L);
        verify(gameRepository).findAllByTitleWithPlatform(newGame.getTitle());
        verify(platformService).getById(newGame.getPlatformId());
        verify(gameRepository).modifyData(eq(oldGame.getTitle()), eq(oldGame.getDeveloper()), eq(oldGame.getPublisher()),
                any(), eq(oldGame.getGenre()), eq(oldGame.getId()));
    }

    @Test
    void whenGameIsFound_delete_removesGame() {
        // Arrange
        long gameId = 1;
        Platform platform = new Platform(1, "testName", "testManufacturer", new Date());
        Game oldGame = new Game(gameId, "testTitle", "testDeveloper", "testPublisher",
                platform, "testGenre");
        Optional<Game> oldGameOptional = Optional.of(oldGame);

        when(gameRepository.findById(gameId)).thenReturn(oldGameOptional);

        // Act
        gameService.delete(gameId);

        // Assert
        verify(gameRepository).findById(gameId);
    }

    @Test
    void whenGameIsNotFound_delete_throwsPlatformNotFound() {
        // Arrange
        long gameId = 1;
        Optional<Game> oldGameOptional = Optional.empty();
        String errorMsg = "Game with id 1 doesn't exist in the database";

        when(gameRepository.findById(gameId)).thenReturn(oldGameOptional);

        // Act / Arrange
        Exception exception = assertThrows(GameNotFound.class, () -> gameService.delete(gameId));
        assertEquals(exception.getMessage(), errorMsg);
        verify(gameRepository).findById(gameId);
    }
}
