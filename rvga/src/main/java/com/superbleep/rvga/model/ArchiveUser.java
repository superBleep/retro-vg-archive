package com.superbleep.rvga.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.sql.Timestamp;

@Entity
@Table(name = "archive_user")
public class ArchiveUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "creation_date")
    @CreationTimestamp
    private Timestamp creationDate;
    @Column(name = "username", unique = true)
    @NotBlank
    private String username;
    @Column(name = "email", unique = true)
    @NotBlank
    @Email
    private String email;
    @Column(name = "password")
    @NotBlank

    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @NotNull
    private ArchiveUserRole role;

    public ArchiveUser() {
    }

    public ArchiveUser(long id, Timestamp creationDate, String username, String email, String password, String firstName, String lastName, ArchiveUserRole role) {
        this.id = id;
        this.creationDate = creationDate;
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public ArchiveUser(String username, String email, String password, String firstName, String lastName, ArchiveUserRole role) {
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

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public ArchiveUserRole getRole() {
        return role;
    }
}
