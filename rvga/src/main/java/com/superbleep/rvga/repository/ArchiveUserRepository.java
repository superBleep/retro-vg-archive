package com.superbleep.rvga.repository;

import com.superbleep.rvga.model.ArchiveUser;
import com.superbleep.rvga.model.ArchiveUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchiveUserRepository extends JpaRepository<ArchiveUser, Long> {
    @Modifying
    @Query("""
        UPDATE ArchiveUser au
        SET au.username = ?1, au.email = ?2, au.firstName = ?3, au.lastName = ?4
        WHERE au.id = ?5
    """)
    void modifyData(String username, String email, String firstName, String lastName, long id);

    @Modifying
    @Query("""
        UPDATE ArchiveUser au
        SET au.password = ?1
        WHERE au.id = ?2
    """)
    void modifyPassword(String password, long id);

    @Modifying
    @Query("""
        UPDATE ArchiveUser au
        SET au.role = ?1
        WHERE au.id = ?2
    """)
    void modifyRole(ArchiveUserRole role, long id);
}
