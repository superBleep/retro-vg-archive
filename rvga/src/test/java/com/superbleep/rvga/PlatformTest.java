package com.superbleep.rvga;

import com.superbleep.rvga.exception.ArchiveUserNotFound;
import com.superbleep.rvga.exception.PlatformEmptyBody;
import com.superbleep.rvga.exception.PlatformNotFound;
import com.superbleep.rvga.model.Platform;
import com.superbleep.rvga.model.PlatformUpdate;
import com.superbleep.rvga.repository.PlatformRepository;
import com.superbleep.rvga.service.PlatformService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlatformTest {
    @InjectMocks
    private PlatformService platformService;

    @Mock
    private PlatformRepository platformRepository;

    @Test
    void whenBodyIsValid_create_savesPlatform() {
        // Arrange
        Platform platform = new Platform(
                "test",
                "testManufacturer",
                new Date()
        );
        Platform savedPlatform = new Platform(
                1,
                "test",
                "testManufacturer",
                new Date()
        );

        when(platformRepository.save(platform)).thenReturn(savedPlatform);

        // Act
        Platform res = platformService.create(platform);

        // Assert
        assertThat(res).isEqualTo(savedPlatform);

        verify(platformRepository).save(platform);
    }

    @Test
    void whenBodyIsValid_create_throwsDataIntegrityViolation() {
        // Arrange
        Platform platform = new Platform(
                "test",
                "testManufacturer",
                new Date()
        );
        String sqlErrorMsg = "ERROR: duplicate key value violates unique constraint \"platform_name_key\"\n  Detail: Key (name)=(test) already exists.";

        when(platformRepository.save(platform)).thenThrow(new DataIntegrityViolationException("", new Throwable(sqlErrorMsg)));

        // Act / Assert
        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> platformService.create(platform));
        assertEquals(exception.getCause().getMessage(), sqlErrorMsg);

        verify(platformRepository).save(platform);
    }

    @Test
    void getAll_returnsAllPlatforms() {
        // Arrange
        Platform platform1 = new Platform(
                1,
                "test1",
                "testManufacturer1",
                new Date()
        );
        Platform platform2 = new Platform(
                2,
                "test1",
                "testManufacturer1",
                new Date()
        );

        when(platformRepository.findAll()).thenReturn(List.of(platform1, platform2));

        // Act
        List<Platform> res = platformService.getAll();

        // Assert
        assertThat(res).hasSize(2);

        verify(platformRepository).findAll();
    }

    @Test
    void whenPlatformFound_getById_returnsPlatform() {
        // Arrange
        Platform platform = new Platform(
                1,
                "test1",
                "testManufacturer1",
                new Date()
        );
        Optional<Platform> platformOptional = Optional.of(platform);

        when(platformRepository.findById(1L)).thenReturn(platformOptional);

        // Act
        Platform res = platformService.getById(1);

        // Assert
        assertThat(res).isEqualTo(platform);

        verify(platformRepository).findById(1L);
    }

    @Test
    void whenPlatformNotFound_getById_throwsPlatformNotFound() {
        // Arrange
        String errorMsg = "Platform with id 1 doesn't exist in the database";
        Optional<Platform> platformOptional = Optional.empty();

        when(platformRepository.findById(1L)).thenReturn(platformOptional);

        // Act / Assert
        Exception exception = assertThrows(PlatformNotFound.class, () -> platformService.getById(1));
        assertEquals(exception.getMessage(), errorMsg);
        verify(platformRepository).findById(1L);
    }

    @Test
    void whenBodyIsValid_modifyData_modifiesData() {
        // Arrange
        long id = 1;
        Platform oldPlatform = new Platform(
                id,
                "test",
                "testManufacturer",
                new Date()
        );
        PlatformUpdate newPlatform = new PlatformUpdate(
                "test1",
                "testManufacturer1",
                new Date()
        );
        Optional<Platform> oldPlatformOptional = Optional.of(oldPlatform);

        when(platformRepository.findById(id)).thenReturn(oldPlatformOptional);

        // Act
        platformService.modifyData(newPlatform, id);

        // Assert
        verify(platformRepository).findById(id);
        verify(platformRepository).modifyData(
                newPlatform.getName(),
                newPlatform.getManufacturer(),
                newPlatform.getRelease(),
                id
        );
    }

    @Test
    void whenPlatformNotFound_modifyData_throwsPlatformNotFound() {
        // Arrange
        long id = 1;
        PlatformUpdate newPlatform = new PlatformUpdate(
                "test1",
                "testManufacturer1",
                new Date()
        );
        Optional<Platform> oldPlatformOptional = Optional.empty();
        String errorMsg = "Platform with id 1 doesn't exist in the database";

        when(platformRepository.findById(id)).thenReturn(oldPlatformOptional);

        // Act / Assert
        Exception exception = assertThrows(PlatformNotFound.class, () -> platformService.modifyData(newPlatform, id));
        assertEquals(exception.getMessage(), errorMsg);
        verify(platformRepository).findById(id);
    }

    @Test
    void whenBodyIsAllNull_modifyData_throwsPlatformEmptyBody() {
        // Arrange
        long id = 1;
        Platform oldPlatform = new Platform(
                id,
                "test",
                "testManufacturer",
                new Date()
        );
        PlatformUpdate newPlatform = new PlatformUpdate(null, null, null);
        Optional<Platform> oldPlatformOptional = Optional.of(oldPlatform);
        String errorMsg = "Request must modify at least one field";

        when(platformRepository.findById(id)).thenReturn(oldPlatformOptional);

        // Act / Assert
        Exception exception = assertThrows(PlatformEmptyBody.class, () -> platformService.modifyData(newPlatform, id));
        assertEquals(exception.getMessage(), errorMsg);
        verify(platformRepository).findById(id);
    }

    @Test
    void whenNameIsNull_modifyData_modifiesData() {
        // Arrange
        long id = 1;
        Platform oldPlatform = new Platform(
                id,
                "test",
                "testManufacturer",
                new Date()
        );
        PlatformUpdate newPlatform = new PlatformUpdate(
                null,
                "testManufacturer1",
                new Date()
        );
        Optional<Platform> oldPlatformOptional = Optional.of(oldPlatform);

        when(platformRepository.findById(id)).thenReturn(oldPlatformOptional);

        // Act
        platformService.modifyData(newPlatform, id);

        // Assert
        verify(platformRepository).findById(id);
        verify(platformRepository).modifyData(
                newPlatform.getName(),
                newPlatform.getManufacturer(),
                newPlatform.getRelease(),
                id
        );
    }

    @Test
    void whenManufacturerIsNull_modifyData_modifiesData() {
        // Arrange
        long id = 1;
        Platform oldPlatform = new Platform(
                id,
                "test",
                "testManufacturer",
                new Date()
        );
        PlatformUpdate newPlatform = new PlatformUpdate(
                "test",
                null,
                new Date()
        );
        Optional<Platform> oldPlatformOptional = Optional.of(oldPlatform);

        when(platformRepository.findById(id)).thenReturn(oldPlatformOptional);

        // Act
        platformService.modifyData(newPlatform, id);

        // Assert
        verify(platformRepository).findById(id);
        verify(platformRepository).modifyData(
                newPlatform.getName(),
                newPlatform.getManufacturer(),
                newPlatform.getRelease(),
                id
        );
    }

    @Test
    void whenReleaseIsNull_modifyData_modifiesData() {
        // Arrange
        long id = 1;
        Platform oldPlatform = new Platform(
                id,
                "test",
                "testManufacturer",
                new Date()
        );
        PlatformUpdate newPlatform = new PlatformUpdate(
                "test",
                "testManufacturer",
                null
        );
        Optional<Platform> oldPlatformOptional = Optional.of(oldPlatform);

        when(platformRepository.findById(id)).thenReturn(oldPlatformOptional);

        // Act
        platformService.modifyData(newPlatform, id);

        // Assert
        verify(platformRepository).findById(id);
        verify(platformRepository).modifyData(
                newPlatform.getName(),
                newPlatform.getManufacturer(),
                newPlatform.getRelease(),
                id
        );
    }

    @Test
    void whenPlatformIsFound_delete_removesPlatform() {
        // Arrange
        long id = 1;
        Platform oldPlatform = new Platform(
                id,
                "test",
                "testManufacturer",
                new Date()
        );
        Optional<Platform> oldPlatformOptional = Optional.of(oldPlatform);

        when(platformRepository.findById(id)).thenReturn(oldPlatformOptional);

        // Act
        platformService.delete(id);

        // Assert
        verify(platformRepository).findById(id);
    }

    @Test
    void whenPlatformIsNotFound_delete_throwsPlatformNotFound() {
        // Arrange
        long id = 1;
        Optional<Platform> oldPlatformOptional = Optional.empty();
        String errorMsg = "Platform with id 1 doesn't exist in the database";

        when(platformRepository.findById(id)).thenReturn(oldPlatformOptional);

        // Act / Arrange
        Exception exception = assertThrows(PlatformNotFound.class, () -> platformService.delete(id));
        assertEquals(exception.getMessage(), errorMsg);
        verify(platformRepository).findById(id);
    }
}