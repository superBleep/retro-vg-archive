package com.superbleep.rvga;

import com.superbleep.rvga.model.EmulatorPlatform;
import com.superbleep.rvga.repository.EmulatorPlatformRepository;
import com.superbleep.rvga.service.EmulatorPlatformService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmulatorPlatformServiceTest {
    @InjectMocks
    private EmulatorPlatformService emulatorPlatformService;

    @Mock
    private EmulatorPlatformRepository emulatorPlatformRepository;

    private static long emulatorId;

    @BeforeAll
    public static void setUp() {
        emulatorId = 1;
    }

    @Test
    public void whenCalled_create_savesPairs() {
        // Act
        emulatorPlatformService.create(any());

        // Assert
        verify(emulatorPlatformRepository).saveAll(any());
    }

    @Test
    public void whenCalled_deleteAllByEmulatorId_removesAllPairs() {
        // Act
        emulatorPlatformService.deleteAllByEmulatorId(emulatorId);

        // Assert
        verify(emulatorPlatformRepository).deleteAllByEmulatorId(emulatorId);
    }
}
