package com.superbleep.nrvga.dto;

import com.superbleep.nrvga.model.ArchiveUserRole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

public class ArchiveUserPost {
    @Column(unique = true)
    @NotBlank(message = "{archive.user.username.not.blank}")
    private String username;

    @Column(unique = true)
    @Email(message = "{archive.user.email.bad.format}")
    @NotBlank(message = "{archive.user.email.not.blank}")
    private String email;

    @NotBlank(message = "{archive.user.password.not.blank}")
    private String password;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @NotNull(message = "{archive.user.role.not.null}")
    private ArchiveUserRole role;

    public ArchiveUserPost(String username, String email, String password, String firstName, String lastName,
                           ArchiveUserRole role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ArchiveUserRole getRole() {
        return role;
    }

    public void setRole(ArchiveUserRole role) {
        this.role = role;
    }
}
