package com.superbleep.rvga.repository;

import com.superbleep.rvga.model.Game;
import com.superbleep.rvga.model.GameVersion;
import com.superbleep.rvga.model.GameVersionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface GameVersionRepository extends JpaRepository<GameVersion, Long> {
    @Query("""
        FROM GameVersion gv
        JOIN FETCH gv.game g
        WHERE gv.id = ?1
    """)
    Optional<GameVersion> findById(GameVersionId id);

    @Modifying
    @Query("""
        UPDATE GameVersion gv
        SET gv.release = ?1, gv.notes = ?2
        WHERE gv.id = ?3
    """)
    void modifyData(Date release, String notes, GameVersionId id);

    @Modifying
    @Query("""
        DELETE GameVersion gv
        WHERE gv.id = ?1
    """)
    void deleteById(GameVersionId id);
}
