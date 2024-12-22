package com.superbleep.rvga.exception;

public class ArchiveUserNotFoundException extends RuntimeException {
    public ArchiveUserNotFoundException(long id) {
        super(STR."User \{id} doesn't exist in the database");
    }
}
