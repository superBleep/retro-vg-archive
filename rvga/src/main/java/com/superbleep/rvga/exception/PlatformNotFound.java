package com.superbleep.rvga.exception;

import com.superbleep.rvga.exception.general.NotFoundException;

public class PlatformNotFound extends NotFoundException {
    public PlatformNotFound(long id) {
        super(STR."Platform with id \{id} doesn't exist in the database");
    }
}
