package com.superbleep.rvga.repository;

import com.superbleep.rvga.dto.GameVersionGet;
import com.superbleep.rvga.model.Game;
import com.superbleep.rvga.model.GameVersion;
import com.superbleep.rvga.model.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    @Query("""
        FROM Game g
        JOIN FETCH g.platform p
        WHERE g.title LIKE ?1
    """)
    List<Game> findAllByTitleWithPlatform(String title);
    @Query("""
        FROM Game g
        JOIN FETCH g.platform p
        WHERE g.id = ?1
    """)
    Optional<Game> findByIdWithPlatform(long id);
    @Query("""
        SELECT new com.superbleep.rvga.dto.GameVersionGet(gv.id.id, gv.game, gv.release, gv.notes)
        FROM GameVersion gv
        WHERE gv.id.gameId = ?1
    """)
    List<GameVersionGet> findAllGameVersions(long id);

    @Modifying
    @Query("""
        UPDATE Game g
        SET g.title = ?1, g.developer = ?2, g.publisher = ?3, g.platform = ?4, g.genre = ?5
        WHERE g.id = ?6
    """)
    void modifyData(String title, String developer, String publisher, Platform platform, String genre, long id);
}
