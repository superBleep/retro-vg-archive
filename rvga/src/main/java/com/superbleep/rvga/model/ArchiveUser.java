package com.superbleep.rvga.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.sql.Timestamp;

@Entity
@Table(name = "archive_user")
public class ArchiveUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "creation_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp creationDate;
    @Column(name = "username")
    private String username;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private ArchiveUserRole role;

    public ArchiveUser() {

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
