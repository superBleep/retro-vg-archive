package com.superbleep.rvga.repository;

import com.superbleep.rvga.dto.ReviewGet;
import com.superbleep.rvga.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("""
        SELECT new com.superbleep.rvga.dto.ReviewGet(r)
        FROM Review r
        WHERE r.gameVersion.id.gameId = ?1
    """)
    List<ReviewGet> findAllByGameId(long gameId);

    @Modifying
    @Query("""
        UPDATE Review r
        SET r.rating = ?1, r.comment = ?2
        WHERE r.id = ?3
    """)
    void modifyData(Integer rating, String comment, long id);
}
