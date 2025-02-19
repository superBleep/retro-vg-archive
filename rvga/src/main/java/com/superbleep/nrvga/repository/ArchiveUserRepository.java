package com.superbleep.nrvga.repository;

import com.superbleep.nrvga.model.ArchiveUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArchiveUserRepository extends JpaRepository<ArchiveUser, Long> {
    Optional<ArchiveUser> findByUsername(String username);

    Optional<ArchiveUser> findByEmail(String email);
}
