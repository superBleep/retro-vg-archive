package com.superbleep.rvga.repository;

import com.superbleep.rvga.model.EmulatorPlatform;
import com.superbleep.rvga.model.EmulatorPlatformId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EmulatorPlatformRepository extends JpaRepository<EmulatorPlatform, EmulatorPlatformId> {
    @Modifying
    @Query("""
        DELETE FROM EmulatorPlatform ep
        WHERE ep.id.emulatorId = ?1
    """)
    void deleteAllByEmulatorId(long emulatorId);
}
