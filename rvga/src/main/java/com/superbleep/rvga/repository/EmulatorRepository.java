package com.superbleep.rvga.repository;

import com.superbleep.rvga.model.Emulator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EmulatorRepository extends JpaRepository<Emulator, Long> {
    @Query("""
        SELECT ep.id.platformId
        FROM EmulatorPlatform ep
        WHERE ep.id.emulatorId = ?1
    """)
    List<Long> findAllPlatformIds(long id);

    @Modifying
    @Query("""
        UPDATE Emulator e
        SET e.name = ?1, e.developer = ?2, e.release = ?3
        WHERE e.id = ?4
    """)
    void modifyData(String name, String developer, Date release, long id);
}