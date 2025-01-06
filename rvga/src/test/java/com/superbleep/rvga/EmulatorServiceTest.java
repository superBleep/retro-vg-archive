package com.superbleep.rvga;

import com.superbleep.rvga.dto.EmulatorGet;
import com.superbleep.rvga.dto.EmulatorPatch;
import com.superbleep.rvga.dto.EmulatorPost;
import com.superbleep.rvga.exception.EmulatorEmptyBody;
import com.superbleep.rvga.exception.EmulatorEmptyPlatformList;
import com.superbleep.rvga.exception.EmulatorNotFound;
import com.superbleep.rvga.exception.PlatformNotFound;
import com.superbleep.rvga.model.Emulator;
import com.superbleep.rvga.model.Platform;
import com.superbleep.rvga.repository.EmulatorRepository;
import com.superbleep.rvga.service.EmulatorPlatformService;
import com.superbleep.rvga.service.EmulatorService;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EmulatorServiceTest {
    @InjectMocks
    private EmulatorService emulatorService;

    @Mock
    private EmulatorRepository emulatorRepository;
    @Mock
    private PlatformService platformService;
    @Mock
    private EmulatorPlatformService emulatorPlatformService;

    private static long id;
    private static Emulator emulator;
    private static EmulatorPost emulatorPost;
    private static EmulatorGet emulatorGet;
    private static Map<String, EmulatorPatch> emulatorPatches;
    private static Platform platform;
    private static List<Long> platformIds;

    @BeforeAll
    public static void setUp() {
        id = 1;
        platform = new Platform(1L, "name", "manufacturer", new Date());
        platformIds = List.of(1L);
        emulator = new Emulator(1L, "name", "developer", new Date());
        emulatorPost = new EmulatorPost("name", "developer", new Date(), platformIds);
        emulatorGet = new EmulatorGet(emulator, platformIds);
        emulatorPatches = Map.of(
                "VALID", new EmulatorPatch("name1", "developer1", new Date()),
                "NAME_NULL", new EmulatorPatch(null, "developer1", new Date()),
                "REST_NULL", new EmulatorPatch("name1", null, null),
                "ALL_NULL", new EmulatorPatch(null, null, null)
        );
    }

    @Test
    public void whenCalled_create_savesEmulator() {
        // Arrange
        when(emulatorRepository.save(any())).thenReturn(emulator);
        when(platformService.getById(emulatorPost.platformIds().getFirst())).thenReturn(platform);

        // Act
        Emulator res = emulatorService.create(emulatorPost);

        // Assert
        assertThat(res).isEqualTo(emulator);

        verify(emulatorRepository).save(any());
        verify(platformService).getById(id);
        verify(emulatorPlatformService).create(any());
    }

    @Test
    public void whenPlatformIsNotFound_create_throwsPlatformNotFound() {
        // Arrange
        when(emulatorRepository.save(any())).thenReturn(emulator);
        when(platformService.getById(emulatorPost.platformIds().getFirst()))
                .thenThrow(new PlatformNotFound(emulatorPost.platformIds().getFirst()));

        // Act & Assert
        assertThrows(PlatformNotFound.class, () -> emulatorService.create(emulatorPost));

        verify(emulatorRepository).save(any());
        verify(platformService).getById(emulatorPost.platformIds().getFirst());
    }

    @Test
    public void whenCalled_getAll_returnsEmulatorList() {
        // Arrange
        when(emulatorRepository.findAll()).thenReturn(List.of(emulator));
        when(emulatorRepository.findAllPlatformIds(emulator.getId()))
                .thenReturn(List.of(platform.getId()));

        // Act
        List<EmulatorGet> res = emulatorService.getAll();

        // Assert
        assertThat(res).usingRecursiveComparison().isEqualTo(List.of(emulatorGet));

        verify(emulatorRepository).findAll();
        verify(emulatorRepository).findAllPlatformIds(id);
    }

    @Test
    public void whenEmulatorIsFound_getById_returnsEmulator() {
        // Arrange
        when(emulatorRepository.findById(id)).thenReturn(Optional.of(emulator));
        when(emulatorRepository.findAllPlatformIds(id)).thenReturn(List.of(platform.getId()));

        // Act
        EmulatorGet res = emulatorService.getById(id);

        // Assert
        assertThat(res).usingRecursiveComparison().isEqualTo(emulatorGet);

        verify(emulatorRepository).findById(id);
        verify(emulatorRepository).findAllPlatformIds(id);
    }

    @Test
    public void whenEmulatorIsNotFound_getById_throwsEmulatorNotFound() {
        // Arrange
        when(emulatorRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EmulatorNotFound.class, () -> emulatorService.getById(id));

        verify(emulatorRepository).findById(id);
    }

    @Test
    public void whenEmulatorIsFound_modifyData_modifiesData() {
        // Arrange
        when(emulatorRepository.findById(id)).thenReturn(Optional.of(emulator));

        // Act
        emulatorService.modifyData(emulatorPatches.get("VALID"), id);

        // Assert
        verify(emulatorRepository).findById(id);
        verify(emulatorRepository).modifyData(any(), any(), any(), eq(id));
    }

    @Test
    public void whenEmulatorIsNotFound_modifyData_throwsEmulatorNotFound() {
        // Arrange
        when(emulatorRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EmulatorNotFound.class, () -> emulatorService.modifyData(emulatorPatches.get("VALID"), id));

        verify(emulatorRepository).findById(id);
    }

    @Test
    public void whenAllFieldsAreNull_modifyData_throwsEmulatorEmptyBody() {
        // Arrange
        when(emulatorRepository.findById(id)).thenReturn(Optional.of(emulator));

        // Act & Assert
        assertThrows(EmulatorEmptyBody.class, () -> emulatorService.modifyData(emulatorPatches.get("ALL_NULL"), id));

        verify(emulatorRepository).findById(id);
    }

    @Test
    public void whenNameIsNull_modifyData_modifiesData() {
        // Arrange
        when(emulatorRepository.findById(id)).thenReturn(Optional.of(emulator));

        // Act
        emulatorService.modifyData(emulatorPatches.get("NAME_NULL"), id);

        verify(emulatorRepository).findById(id);
        verify(emulatorRepository).modifyData(any(), any(), any(), eq(id));
    }

    @Test
    public void whenAllButNameAreNull_modifyData_modifiesData() {
        // Arrange
        when(emulatorRepository.findById(id)).thenReturn(Optional.of(emulator));

        // Act
        emulatorService.modifyData(emulatorPatches.get("REST_NULL"), id);

        verify(emulatorRepository).findById(id);
        verify(emulatorRepository).modifyData(any(), any(), any(), eq(id));
    }

    @Test
    public void whenPlatformsFound_modifyPlatforms_modifiesPlatforms() {
        // Arrange
        when(emulatorRepository.findById(id)).thenReturn(Optional.of(emulator));
        when(platformService.getById(platformIds.getFirst())).thenReturn(platform);

        // Act
        emulatorService.modifyPlatforms(platformIds, id);

        verify(emulatorRepository).findById(id);
        verify(platformService).getById(id);
        verify(emulatorPlatformService).create(any());
    }

    @Test
    public void whenPlatformIdsIsEmpty_modifyPlatforms_throwsEmulatorEmptyPlatformList() {
        // Act & Assert
        assertThrows(EmulatorEmptyPlatformList.class, () -> emulatorService.modifyPlatforms(List.of(), id));
    }

    @Test
    public void whenEmulatorFound_delete_removesEmulator() {
        // Arrange
        when(emulatorRepository.findById(id)).thenReturn(Optional.of(emulator));

        // Act
        emulatorService.delete(id);

        verify(emulatorRepository).findById(id);
        verify(emulatorRepository).deleteById(id);
    }

    @Test
    public void whenEmulatorIsNotFound_delet_throwsEmulatorNotFound() {
        // Arrange
        when(emulatorRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EmulatorNotFound.class, () -> emulatorService.delete(id));
    }
}
