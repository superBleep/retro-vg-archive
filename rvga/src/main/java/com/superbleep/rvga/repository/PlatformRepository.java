package com.superbleep.rvga.repository;

import com.superbleep.rvga.dto.GameGet;
import com.superbleep.rvga.model.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {
    @Query("""
        SELECT new com.superbleep.rvga.dto.GameGet(g.id, g.title, g.developer, g.publisher, g.genre)
        FROM Game g
        WHERE g.platform.id = ?1
    """)
    List<GameGet> findAllGames(long id);

    @Modifying
    @Query("""
        UPDATE Platform p
        SET p.name = ?1, p.manufacturer = ?2, p.release = ?3
        WHERE p.id = ?4
    """)
    void modifyData(String name, String manufacturer, Date release, long id);
}
