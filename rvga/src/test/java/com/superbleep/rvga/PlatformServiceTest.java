package com.superbleep.rvga;

import com.superbleep.rvga.dto.GameGet;
import com.superbleep.rvga.dto.PlatformPatch;
import com.superbleep.rvga.exception.PlatformEmptyBody;
import com.superbleep.rvga.exception.PlatformNotFound;
import com.superbleep.rvga.model.Platform;
import com.superbleep.rvga.repository.PlatformRepository;
import com.superbleep.rvga.service.PlatformService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
public class PlatformServiceTest {
    @InjectMocks
    private PlatformService platformService;

    @Mock
    private PlatformRepository platformRepository;

    private static long id;
    private static Platform platfrom;
    private static Platform savedPlatform;
    private static Map<String, PlatformPatch> platformPatches;
    private static GameGet persistedGame;

    @BeforeAll
    public static void setUp() {
        id = 1;
        platfrom = new Platform("test", "manufacturer", new Date());
        savedPlatform = new Platform(1, "test", "manufacturer", new Date());
        platformPatches = Map.of(
                "VALID", new PlatformPatch("name1", "manufacturer1", new Date()),
                "NAME_NULL", new PlatformPatch(null, "manufacturer1", new Date()),
                "REST_NULL", new PlatformPatch("name1", null, null),
                "ALL_NULL", new PlatformPatch(null, null, null)
        );
        persistedGame = new GameGet(1, "title", "developer", "publisher",
                "genre");
    }

    @Test
    void whenCalled_create_savesPlatform() {
        // Arrange
        when(platformRepository.save(platfrom)).thenReturn(savedPlatform);

        // Act
        Platform res = platformService.create(platfrom);

        // Assert
        assertThat(res).isEqualTo(savedPlatform);

        verify(platformRepository).save(platfrom);
    }

    @Test
    void whenCalled_getAll_returnsAllPlatforms() {
        // Arrange
        when(platformRepository.findAll()).thenReturn(List.of(savedPlatform));

        // Act
        List<Platform> res = platformService.getAll();

        // Assert
        assertThat(res).usingRecursiveComparison().isEqualTo(List.of(savedPlatform));

        verify(platformRepository).findAll();
    }

    @Test
    void whenPlatformIsFound_getById_returnsPlatform() {
        // Arrange
        when(platformRepository.findById(id)).thenReturn(Optional.of(savedPlatform));

        // Act
        Platform res = platformService.getById(id);

        // Assert
        assertThat(res).isEqualTo(savedPlatform);

        verify(platformRepository).findById(id);
    }

    @Test
    void whenPlatformNotFound_getById_throwsPlatformNotFound() {
        // Arrange
        when(platformRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PlatformNotFound.class, () -> platformService.getById(id));

        verify(platformRepository).findById(id);
    }

    @Test
    void whenCalled_getGames_returnsGameList() {
        // Arrange
        when(platformRepository.findById(id)).thenReturn(Optional.of(savedPlatform));
        when(platformRepository.findAllGames(id)).thenReturn(List.of(persistedGame));

        // Act
        List<GameGet> res = platformService.getGames(id);

        // Assert
        assertThat(res).usingRecursiveComparison().isEqualTo(List.of(persistedGame));

        verify(platformRepository).findById(id);
        verify(platformRepository).findAllGames(id);
    }

    @Test
    void whenPlatformIsNotFound_getGames_throwsPlatformNotFound() {
        // Arrange
        when(platformRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PlatformNotFound.class, () -> platformService.getById(id));

        verify(platformRepository).findById(id);
    }

    @Test
    void whenPlatformFound_modifyData_modifiesData() {
        // Arrange
        when(platformRepository.findById(id)).thenReturn(Optional.of(savedPlatform));

        // Act
        platformService.modifyData(platformPatches.get("VALID"), id);

        // Assert
        verify(platformRepository).findById(id);
        verify(platformRepository).modifyData(any(), any(), any(), eq(id));
    }

    @Test
    void whenPlatformIsNotFound_modifyData_throwsPlatformNotFound() {
        // Arrange
        when(platformRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PlatformNotFound.class, () -> platformService.modifyData(platformPatches.get("VALID"), id));

        verify(platformRepository).findById(id);
    }

    @Test
    void whenAllFieldsAreNull_modifyData_throwsPlatformEmptyBody() {
        // Arrange
        when(platformRepository.findById(id)).thenReturn(Optional.of(savedPlatform));

        // Act & Assert
        assertThrows(PlatformEmptyBody.class, () -> platformService.modifyData(platformPatches.get("ALL_NULL"), id));

        verify(platformRepository).findById(id);
    }

    @Test
    void whenNameIsNull_modifyData_modifiesData() {
        // Arrange
        when(platformRepository.findById(id)).thenReturn(Optional.of(savedPlatform));

        // Act
        platformService.modifyData(platformPatches.get("NAME_NULL"), id);

        // Assert
        verify(platformRepository).findById(id);
        verify(platformRepository).modifyData(any(), any(), any(), eq(id));
    }

    @Test
    void whenAllButNameAreNull_modifyData_modifiesData() {
        // Arrange
        when(platformRepository.findById(id)).thenReturn(Optional.of(savedPlatform));

        // Act
        platformService.modifyData(platformPatches.get("REST_NULL"), id);

        // Assert
        verify(platformRepository).findById(id);
        verify(platformRepository).modifyData(any(), any(), any(), eq(id));
    }

    @Test
    void whenPlatformIsFound_delete_removesPlatform() {
        // Arrange
        when(platformRepository.findById(id)).thenReturn(Optional.of(savedPlatform));

        // Act
        platformService.delete(id);

        // Assert
        verify(platformRepository).findById(id);
        verify(platformRepository).deleteById(id);
    }

    @Test
    void whenPlatformIsNotFound_delete_throwsPlatformNotFound() {
        // Arrange
        when(platformRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PlatformNotFound.class, () -> platformService.delete(id));

        verify(platformRepository).findById(id);
    }
}
