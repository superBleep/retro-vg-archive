package com.superbleep.rvga.repository;

import com.superbleep.rvga.model.ArchiveUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchiveUserRepository extends JpaRepository<ArchiveUser, Long> {

}
