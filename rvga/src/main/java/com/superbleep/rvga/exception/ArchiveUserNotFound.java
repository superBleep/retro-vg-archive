package com.superbleep.rvga.exception;

import com.superbleep.rvga.exception.general.NotFoundException;

public class ArchiveUserNotFound extends NotFoundException {
    public ArchiveUserNotFound(long id) {
        super(STR."User with id \{id} doesn't exist in the database");
    }
}
