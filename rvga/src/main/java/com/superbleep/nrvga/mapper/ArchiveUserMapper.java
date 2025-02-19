package com.superbleep.nrvga.mapper;

import com.superbleep.nrvga.dto.ArchiveUserPost;
import com.superbleep.nrvga.model.ArchiveUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArchiveUserMapper {
    ArchiveUser toArchiveUser(ArchiveUserPost archiveUserPost);
}
