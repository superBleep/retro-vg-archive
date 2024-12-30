package com.superbleep.rvga;

import com.superbleep.rvga.exception.*;
import com.superbleep.rvga.model.ArchiveUser;
import com.superbleep.rvga.model.ArchiveUserRole;
import com.superbleep.rvga.dto.ArchiveUserPatch;
import com.superbleep.rvga.repository.ArchiveUserRepository;
import com.superbleep.rvga.service.ArchiveUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArchiveUserServiceTest {
    @InjectMocks
    private ArchiveUserService archiveUserService;
    @Mock
    private ArchiveUserRepository archiveUserRepository;

    @Test
    void whenBodyIsValid_create_savesArchiveUser() {
        // Arrange
        ArchiveUser archiveUser = new ArchiveUser("testUsername", "test@email.com", "1234",
                "Test", "Test", ArchiveUserRole.regular);
        ArchiveUser savedArchiveUser = new ArchiveUser(
                1, new Timestamp(System.currentTimeMillis()), "testUsername", "test@email.com",
                "1234", "Test", "Test", ArchiveUserRole.regular);

        when(archiveUserRepository.save(archiveUser)).thenReturn(savedArchiveUser);

        // Act
        ArchiveUser res = archiveUserService.create(archiveUser);

        // Assert
        assertThat(res).isEqualTo(savedArchiveUser);

        verify(archiveUserRepository).save(archiveUser);
    }

    @Test
    void whenBodyIsValid_create_throwsDataIntegrityViolation() {
        // Arrange
        ArchiveUser archiveUser = new ArchiveUser("testUsername", "test@email.com", "1234",
                "Test", "Test", ArchiveUserRole.regular);
        String sqlErrorMsg = "ERROR: duplicate key value violates unique constraint \"archive_user_username_key\"\n" +
                "  Detail: Key (username)=(testUsername) already exists.";

        when(archiveUserRepository.save(archiveUser)).thenThrow(new DataIntegrityViolationException("",
                new Throwable(sqlErrorMsg)));

        // Act / Assert
        Exception exception = assertThrows(DataIntegrityViolationException.class,
                () -> archiveUserService.create(archiveUser));
        assertEquals(exception.getCause().getMessage(), sqlErrorMsg);

        verify(archiveUserRepository).save(archiveUser);
    }

    @Test
    void getAll_returnsAllArchiveUsers() {
        // Arrange
        ArchiveUser archiveUser1 = new ArchiveUser(1, new Timestamp(System.currentTimeMillis()),
                "testUsername", "test@email.com", "1234", "Test", "Test",
                ArchiveUserRole.regular);
        ArchiveUser archiveUser2 = new ArchiveUser(2, new Timestamp(System.currentTimeMillis()),
                "testUsername2", "test2t@email.com", "1234", "Test", "Test",
                ArchiveUserRole.regular);

        when(archiveUserRepository.findAll()).thenReturn(List.of(archiveUser1, archiveUser2));

        // Act
        List<ArchiveUser> res = archiveUserService.getAll();

        // Assert
        assertThat(res).hasSize(2);

        verify(archiveUserRepository).findAll();
    }

    @Test
    void whenUserFound_getById_returnsArchiveUser() {
        // Arrange
        ArchiveUser archiveUser = new ArchiveUser(1, new Timestamp(System.currentTimeMillis()),
                "testUsername", "test@email.com", "1234", "Test", "Test",
                ArchiveUserRole.regular);
        Optional<ArchiveUser> archiveUserOptional = Optional.of(archiveUser);

        when(archiveUserRepository.findById(1L)).thenReturn(archiveUserOptional);

        // Act
        ArchiveUser res = archiveUserService.getById(1);

        // Assert
        assertThat(res).isEqualTo(archiveUser);

        verify(archiveUserRepository).findById(1L);
    }

    @Test
    void whenUserNotFound_getById_throwsArchiveUserNotFound() {
        // Arrange
        String errorMsg = "User with id 1 doesn't exist in the database";
        Optional<ArchiveUser> archiveUserOptional = Optional.empty();

        when(archiveUserRepository.findById(1L)).thenReturn(archiveUserOptional);

        // Act / Assert
        Exception exception = assertThrows(ArchiveUserNotFound.class, () -> archiveUserService.getById(1));
        assertEquals(exception.getMessage(), errorMsg);
        verify(archiveUserRepository).findById(1L);
    }

    @Test
    void whenBodyIsValid_modifyData_modifiesData() {
        // Arrrange
        long id = 1;
        ArchiveUser oldUser = new ArchiveUser(id, new Timestamp(System.currentTimeMillis()), "testUsername",
                "test@email.com", "1234", "Test", "Test", ArchiveUserRole.regular);
        ArchiveUserPatch newUser = new ArchiveUserPatch("test1Username", "test1@email.com",
                "Test1", "Test1");
        Optional<ArchiveUser> oldUserOptional = Optional.of(oldUser);

        when(archiveUserRepository.findById(id)).thenReturn(oldUserOptional);

        // Act
        archiveUserService.modifyData(newUser, id);

        // Assert
        verify(archiveUserRepository).findById(id);
        verify(archiveUserRepository).modifyData(newUser.getUsername(), newUser.getEmail(), newUser.getFirstName(),
                newUser.getLastName(), id);
    }

    @Test
    void whenUserNotFound_modifyData_throwArchiveUserNotFound() {
        // Arrange
        long id = 1;
        ArchiveUserPatch newUser = new ArchiveUserPatch("test1Username", "test1@email.com",
                "Test1", "Test1");
        Optional<ArchiveUser> oldUserOptional = Optional.empty();
        String errorMsg = "User with id 1 doesn't exist in the database";

        when(archiveUserRepository.findById(id)).thenReturn(oldUserOptional);

        // Act / Assert
        Exception exception = assertThrows(ArchiveUserNotFound.class, () -> archiveUserService.modifyData(newUser, id));
        assertEquals(exception.getMessage(), errorMsg);
        verify(archiveUserRepository).findById(id);
    }

    @Test
    void whenBodyIsAllNull_modifyData_throwsArchiveUserEmptyBody() {
        // Arrange
        long id = 1;
        ArchiveUser oldUser = new ArchiveUser(id, new Timestamp(System.currentTimeMillis()), "testUsername",
                "test@email.com", "1234", "Test", "Test", ArchiveUserRole.regular);
        ArchiveUserPatch newUser = new ArchiveUserPatch(null,null,null,null);
        Optional<ArchiveUser> oldUserOptional = Optional.of(oldUser);
        String errorMsg = "Request must modify at least one field";

        when(archiveUserRepository.findById(id)).thenReturn(oldUserOptional);

        // Act / Assert
        Exception exception = assertThrows(ArchiveUserEmptyBody.class, () -> archiveUserService.modifyData(newUser, id));
        assertEquals(exception.getMessage(), errorMsg);
        verify(archiveUserRepository).findById(id);
    }

    @Test
    void whenUsernameIsNull_modifyData_modifiesData() {
        // Arrrange
        long id = 1;
        ArchiveUser oldUser = new ArchiveUser(id, new Timestamp(System.currentTimeMillis()), "testUsername",
                "test@email.com", "1234", "Test", "Test", ArchiveUserRole.regular);
        ArchiveUserPatch newUser = new ArchiveUserPatch(null, "test1@email.com", "Test1",
                "Test1");
        Optional<ArchiveUser> oldUserOptional = Optional.of(oldUser);

        when(archiveUserRepository.findById(id)).thenReturn(oldUserOptional);

        // Act
        archiveUserService.modifyData(newUser, id);

        // Assert
        verify(archiveUserRepository).findById(id);
        verify(archiveUserRepository).modifyData(newUser.getUsername(), newUser.getEmail(), newUser.getFirstName(),
                newUser.getLastName(), id);
    }

    @Test
    void whenAllButUsernameIsNull_modifyData_modifiesData() {
        // Arrrange
        long id = 1;
        ArchiveUser oldUser = new ArchiveUser(id, new Timestamp(System.currentTimeMillis()), "testUsername",
                "test@email.com", "1234", "Test", "Test", ArchiveUserRole.regular);
        ArchiveUserPatch newUser = new ArchiveUserPatch("testUsername1", null, null,
                null);
        Optional<ArchiveUser> oldUserOptional = Optional.of(oldUser);

        when(archiveUserRepository.findById(id)).thenReturn(oldUserOptional);

        // Act
        archiveUserService.modifyData(newUser, id);

        // Assert
        verify(archiveUserRepository).findById(id);
        verify(archiveUserRepository).modifyData(newUser.getUsername(), newUser.getEmail(), newUser.getFirstName(),
                newUser.getLastName(), id);
    }

    @Test
    void whenPasswordIsValid_modifyPassword_modifiesPassword() {
        // Arrrange
        long id = 1;
        ArchiveUser oldUser = new ArchiveUser(id, new Timestamp(System.currentTimeMillis()), "testUsername",
                "test@email.com", "1234", "Test", "Test", ArchiveUserRole.regular);
        Optional<ArchiveUser> oldUserOptional = Optional.of(oldUser);
        String newPassword = "12345";

        when(archiveUserRepository.findById(id)).thenReturn(oldUserOptional);

        // Act
        archiveUserService.modifyPassword(newPassword, id);

        // Assert
        verify(archiveUserRepository).findById(id);
        verify(archiveUserRepository).modifyPassword(newPassword, id);
    }

    @Test
    void whenUserNotFound_modifyPassword_throwsArchiveUserNotFound() {
        // Arrange
        long id = 1;
        Optional<ArchiveUser> oldUserOptional = Optional.empty();
        String errorMsg = "User with id 1 doesn't exist in the database";
        String newPassword = "12345";

        when(archiveUserRepository.findById(id)).thenReturn(oldUserOptional);

        // Act / Assert
        Exception exception = assertThrows(ArchiveUserNotFound.class,
                () -> archiveUserService.modifyPassword(newPassword, id));
        assertEquals(exception.getMessage(), errorMsg);
        verify(archiveUserRepository).findById(id);
    }

    @Test
    void whenPasswordIdentical_modifyPassword_throwsArchiveUserPasswordsIdentical() {
        // Arrange
        long id = 1;
        ArchiveUser oldUser = new ArchiveUser(id, new Timestamp(System.currentTimeMillis()), "testUsername",
                "test@email.com", "1234", "Test", "Test", ArchiveUserRole.regular);
        Optional<ArchiveUser> oldUserOptional = Optional.of(oldUser);
        String errorMsg = "The new password must be different from the old one";
        String newPassword = "1234";

        when(archiveUserRepository.findById(id)).thenReturn(oldUserOptional);

        // Act / Assert
        Exception exception = assertThrows(ArchiveUserPasswordsIdentical.class,
                () -> archiveUserService.modifyPassword(newPassword, id));
        assertEquals(exception.getMessage(), errorMsg);
        verify(archiveUserRepository).findById(id);
    }

    @Test
    void whenBodyIsValid_modifyRole_modifiesRole() {
        // Arrange
        long id = 1;
        ArchiveUser oldUser = new ArchiveUser(id, new Timestamp(System.currentTimeMillis()), "testUsername",
                "test@email.com", "1234", "Test", "Test", ArchiveUserRole.admin);
        Optional<ArchiveUser> oldUserOptional = Optional.of(oldUser);
        String newRoleString = "moderator";

        when(archiveUserRepository.findById(id)).thenReturn(oldUserOptional);

        // Act
        archiveUserService.modifyRole(newRoleString, id);

        // Assert
        verify(archiveUserRepository).findById(id);
        verify(archiveUserRepository).modifyRole(ArchiveUserRole.moderator, id);
    }

    @Test
    void whenUserNotFound_modifyRole_throwsArchiveUserNotFound() {
        // Arrange
        long id = 1;
        Optional<ArchiveUser> oldUserOptional = Optional.empty();
        String errorMsg = "User with id 1 doesn't exist in the database";
        String newRoleString = "moderator";

        when(archiveUserRepository.findById(id)).thenReturn(oldUserOptional);

        // Act / Assert
        Exception exception = assertThrows(ArchiveUserNotFound.class,
                () -> archiveUserService.modifyRole(newRoleString, id));
        assertEquals(exception.getMessage(), errorMsg);
        verify(archiveUserRepository).findById(id);
    }

    @Test
    void whenRoleNotStrongEnough_modifyRole_throwsArchiveUserRolesForbidden() {
        // Arrange
        long id = 1;
        ArchiveUser oldUser = new ArchiveUser(id, new Timestamp(System.currentTimeMillis()), "testUsername",
                "test@email.com", "1234", "Test", "Test", ArchiveUserRole.regular);
        Optional<ArchiveUser> oldUserOptional = Optional.of(oldUser);
        String errorMsg = "Only admins can change the roles of other users";
        String newRoleString = "admin";

        when(archiveUserRepository.findById(id)).thenReturn(oldUserOptional);

        // Act / Assert
        Exception exception = assertThrows(ArchiveUserRolesForbidden.class,
                () -> archiveUserService.modifyRole(newRoleString, id));
        assertEquals(exception.getMessage(), errorMsg);
        verify(archiveUserRepository).findById(id);
    }

    @Test
    void whenRoleNotFound_modifyRole_throwsArchiveUserRoleNotFound() {
        // Arrange
        long id = 1;
        ArchiveUser oldUser = new ArchiveUser(id, new Timestamp(System.currentTimeMillis()), "testUsername",
                "test@email.com", "1234", "Test", "Test", ArchiveUserRole.admin);
        Optional<ArchiveUser> oldUserOptional = Optional.of(oldUser);
        String errorMsg = "Request user role not found";
        String newRoleString = "superUser";

        when(archiveUserRepository.findById(id)).thenReturn(oldUserOptional);

        // Act / Assert
        Exception exception = assertThrows(ArchiveUserRoleNotFound.class,
                () -> archiveUserService.modifyRole(newRoleString, id));
        assertEquals(exception.getMessage(), errorMsg);
        verify(archiveUserRepository).findById(id);
    }

    @Test
    void whenRolesIdentical_modifyRole_throwsArchiveUserRolesIdentical() {
        // Arrange
        long id = 1;
        ArchiveUser oldUser = new ArchiveUser(id, new Timestamp(System.currentTimeMillis()), "testUsername",
                "test@email.com", "1234", "Test", "Test", ArchiveUserRole.admin);
        Optional<ArchiveUser> oldUserOptional = Optional.of(oldUser);
        String errorMsg = "The new role must be different from the old one";
        String newRoleString = "admin";

        when(archiveUserRepository.findById(id)).thenReturn(oldUserOptional);

        // Act / Assert
        Exception exception = assertThrows(ArchiveUserRolesIdentical.class,
                () -> archiveUserService.modifyRole(newRoleString, id));
        assertEquals(exception.getMessage(), errorMsg);
        verify(archiveUserRepository).findById(id);
    }

    @Test
    void whenUserIsFound_delete_removesUser() {
        // Arrange
        long id = 1;
        ArchiveUser oldUser = new ArchiveUser(id, new Timestamp(System.currentTimeMillis()), "testUsername",
                "test@email.com", "1234", "Test", "Test", ArchiveUserRole.regular);
        Optional<ArchiveUser> oldUserOptional = Optional.of(oldUser);

        when(archiveUserRepository.findById(id)).thenReturn(oldUserOptional);

        // Act
        archiveUserService.delete(id);

        // Assert
        verify(archiveUserRepository).findById(id);
    }

    @Test
    void whenUserIsNotFound_delete_throwsArchiveUserNotFound() {
        // Arrange
        long id = 1;
        Optional<ArchiveUser> oldUserOptional = Optional.empty();
        String errorMsg = "User with id 1 doesn't exist in the database";

        when(archiveUserRepository.findById(id)).thenReturn(oldUserOptional);

        // Act / Arrange
        Exception exception = assertThrows(ArchiveUserNotFound.class, () -> archiveUserService.delete(id));
        assertEquals(exception.getMessage(), errorMsg);
        verify(archiveUserRepository).findById(id);
    }
}
