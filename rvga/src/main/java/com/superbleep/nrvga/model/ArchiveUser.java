package com.superbleep.nrvga.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.context.MessageSource;

import java.sql.Timestamp;

@Entity
public class ArchiveUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreationTimestamp
    private Timestamp creationTimestamp;

    @Column(unique = true)
    @NotBlank
    private String username;

    @Column(unique = true)
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @NotNull
    private ArchiveUserRole role;

    public ArchiveUser() {
    }

    public ArchiveUser(long id, Timestamp creationTimestamp, String username, String email, String password,
                       String firstName, String lastName, ArchiveUserRole role) {
        this.id = id;
        this.creationTimestamp = creationTimestamp;
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
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
