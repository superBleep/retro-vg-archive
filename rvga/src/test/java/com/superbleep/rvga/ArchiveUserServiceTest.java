package com.superbleep.rvga;

import com.superbleep.rvga.dto.ArchiveUserPatch;
import com.superbleep.rvga.exception.*;
import com.superbleep.rvga.model.ArchiveUser;
import com.superbleep.rvga.model.ArchiveUserRole;
import com.superbleep.rvga.repository.ArchiveUserRepository;
import com.superbleep.rvga.service.ArchiveUserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
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
public class ArchiveUserServiceTest {
    @InjectMocks
    private ArchiveUserService archiveUserService;
    @Mock
    private ArchiveUserRepository archiveUserRepository;

    private static long id;
    private static ArchiveUser archiveUser;
    private static ArchiveUser adminArchiveUser;
    private static ArchiveUser savedArchiveUser;
    private static Map<String, ArchiveUserPatch> archiveUserPatches;

    @BeforeAll
    static void setUp() {
        id = 1;
        archiveUser = new ArchiveUser("username", "email@mail.com", "1234",
                "firstName", "lastName", ArchiveUserRole.regular);
        adminArchiveUser = new ArchiveUser("username", "email@mail.com", "1234",
                "firstName", "lastName", ArchiveUserRole.admin);
        savedArchiveUser = new ArchiveUser(1L, new Timestamp(System.currentTimeMillis()), "username",
                "email@mail.com", "1234", "firstName", "lastName",
                ArchiveUserRole.regular);
        archiveUserPatches = Map.of(
                "VALID", new ArchiveUserPatch("username1", "email1@mail.com", "firstName1",
                        "lastName1"),
                "USERNAME_NULL", new ArchiveUserPatch(null, "email1@mail.com", "firstName1",
                        "lastName1"),
                "REST_NULL", new ArchiveUserPatch("username1", null, null, null),
                "ALL_NULL", new ArchiveUserPatch(null, null, null, null)
        );
    }

    @Test
    void whenCalled_create_savesArchiveUser() {
        // Arrange
        when(archiveUserRepository.save(archiveUser)).thenReturn(savedArchiveUser);

        // Act
        ArchiveUser res = archiveUserService.create(archiveUser);

        // Assert
        assertThat(res).isEqualTo(savedArchiveUser);

        verify(archiveUserRepository).save(archiveUser);
    }

    @Test
    void whenCalled_getAll_returnsArchiveUserList() {
        // Arrange
        when(archiveUserRepository.findAll()).thenReturn(List.of(archiveUser));

        // Act
        List<ArchiveUser> res = archiveUserService.getAll();

        // Assert
        assertThat(res).usingRecursiveComparison().isEqualTo(List.of(archiveUser));

        verify(archiveUserRepository).findAll();
    }

    @Test
    void whenUserIsFound_getById_returnsArchiveUser() {
        // Arrange
        when(archiveUserRepository.findById(id)).thenReturn(Optional.of(archiveUser));

        // Act
        ArchiveUser res = archiveUserService.getById(id);

        // Assert
        assertThat(res).isEqualTo(archiveUser);

        verify(archiveUserRepository).findById(id);
    }

    @Test
    void whenUserIsNotFound_getById_throwsArchiveUserNotFound() {
        // Arrange
        when(archiveUserRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ArchiveUserNotFound.class, () -> archiveUserService.getById(id));

        verify(archiveUserRepository).findById(id);
    }

    @Test
    void whenUserIsFound_modifyData_modifiesData() {
        // Arrrange
        when(archiveUserRepository.findById(id)).thenReturn(Optional.of(archiveUser));

        // Act
        archiveUserService.modifyData(archiveUserPatches.get("VALID"), id);

        // Assert
        verify(archiveUserRepository).findById(id);
        verify(archiveUserRepository).modifyData(any(), any(), any(), any(), eq(id));
    }

    @Test
    void whenUserIsNotFound_modifyData_throwArchiveUserNotFound() {
        // Arrange
        when(archiveUserRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ArchiveUserNotFound.class,
                () -> archiveUserService.modifyData(archiveUserPatches.get("VALID"), id));

        verify(archiveUserRepository).findById(id);
    }

    @Test
    void whenAllFieldsAreNull_modifyData_throwsArchiveUserEmptyBody() {
        // Arrange
        when(archiveUserRepository.findById(id)).thenReturn(Optional.of(archiveUser));

        // Act & Assert
        assertThrows(ArchiveUserEmptyBody.class,
                () -> archiveUserService.modifyData(archiveUserPatches.get("ALL_NULL"), id));

        verify(archiveUserRepository).findById(id);
    }

    @Test
    void whenUsernameIsNull_modifyData_modifiesData() {
        // Arrrange
        when(archiveUserRepository.findById(id)).thenReturn(Optional.of(archiveUser));

        // Act
        archiveUserService.modifyData(archiveUserPatches.get("USERNAME_NULL"), id);

        // Assert
        verify(archiveUserRepository).findById(id);
        verify(archiveUserRepository).modifyData(any(), any(), any(), any(), eq(id));
    }

    @Test
    void whenAllButUsernameAreNull_modifyData_modifiesData() {
        // Arrrange
        when(archiveUserRepository.findById(id)).thenReturn(Optional.of(archiveUser));

        // Act
        archiveUserService.modifyData(archiveUserPatches.get("REST_NULL"), id);

        // Assert
        verify(archiveUserRepository).findById(id);
        verify(archiveUserRepository).modifyData(any(), any(), any(), any(), eq(id));
    }

    @Test
    void whenUserIsFound_modifyPassword_modifiesPassword() {
        // Arrrange
        String newPassword = "new";
        when(archiveUserRepository.findById(id)).thenReturn(Optional.of(archiveUser));

        // Act
        archiveUserService.modifyPassword(newPassword, id);

        // Assert
        verify(archiveUserRepository).findById(id);
        verify(archiveUserRepository).modifyPassword(newPassword, id);
    }

    @Test
    void whenUserIsNotFound_modifyPassword_throwsArchiveUserNotFound() {
        // Arrange
        String newPassword = "new";
        when(archiveUserRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ArchiveUserNotFound.class, () -> archiveUserService.modifyPassword(newPassword, id));

        verify(archiveUserRepository).findById(id);
    }

    @Test
    void whenPasswordIsIdentical_modifyPassword_throwsArchiveUserPasswordsIdentical() {
        // Arrange
        String newPassword = archiveUser.getPassword();

        when(archiveUserRepository.findById(id)).thenReturn(Optional.of(archiveUser));

        // Act & Assert
        assertThrows(ArchiveUserPasswordsIdentical.class, () -> archiveUserService.modifyPassword(newPassword, id));

        verify(archiveUserRepository).findById(id);
    }

    @Test
    void whenUserIsFound_modifyRole_modifiesRole() {
        // Arrange
        String newRoleString = "moderator";
        when(archiveUserRepository.findById(id)).thenReturn(Optional.of(adminArchiveUser));

        // Act
        archiveUserService.modifyRole(newRoleString, id);

        // Assert
        verify(archiveUserRepository).findById(id);
        verify(archiveUserRepository).modifyRole(ArchiveUserRole.moderator, id);
    }

    @Test
    void whenUserIsNotFound_modifyRole_throwsArchiveUserNotFound() {
        // Arrange
        String newRoleString = "moderator";
        when(archiveUserRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ArchiveUserNotFound.class, () -> archiveUserService.modifyRole(newRoleString, id));

        verify(archiveUserRepository).findById(id);
    }

    @Test
    void whenRoleIsNotPrivilegedEnough_modifyRole_throwsArchiveUserRolesForbidden() {
        // Arrange
        String newRoleString = "admin";
        when(archiveUserRepository.findById(id)).thenReturn(Optional.of(archiveUser));

        // Act & Assert
        assertThrows(ArchiveUserRolesForbidden.class, () -> archiveUserService.modifyRole(newRoleString, id));

        verify(archiveUserRepository).findById(id);
    }

    @Test
    void whenRoleIsNotFound_modifyRole_throwsArchiveUserRoleNotFound() {
        // Arrange
        String newRoleString = "superUser";
        when(archiveUserRepository.findById(id)).thenReturn(Optional.of(adminArchiveUser));

        // Act & Assert
        assertThrows(ArchiveUserRoleNotFound.class, () -> archiveUserService.modifyRole(newRoleString, id));

        verify(archiveUserRepository).findById(id);
    }

    @Test
    void whenRolesAreIdentical_modifyRole_throwsArchiveUserRolesIdentical() {
        // Arrange
        String newRoleString = "admin";
        when(archiveUserRepository.findById(id)).thenReturn(Optional.of(adminArchiveUser));

        // Act & Assert
        assertThrows(ArchiveUserRolesIdentical.class, () -> archiveUserService.modifyRole(newRoleString, id));

        verify(archiveUserRepository).findById(id);
    }

    @Test
    void whenUserIsFound_delete_removesUser() {
        // Arrange
        when(archiveUserRepository.findById(id)).thenReturn(Optional.of(archiveUser));

        // Act
        archiveUserService.delete(id);

        // Assert
        verify(archiveUserRepository).findById(id);
        verify(archiveUserRepository).deleteById(id);
    }

    @Test
    void whenUserIsNotFound_delete_throwsArchiveUserNotFound() {
        // Arrange
        when(archiveUserRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ArchiveUserNotFound.class, () -> archiveUserService.delete(id));

        verify(archiveUserRepository).findById(id);
    }
}
