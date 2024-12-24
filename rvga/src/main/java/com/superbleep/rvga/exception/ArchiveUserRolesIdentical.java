package com.superbleep.rvga.exception;

import com.superbleep.rvga.exception.general.BadRequestException;

public class ArchiveUserRolesIdentical extends BadRequestException {
    public ArchiveUserRolesIdentical() {
        super("The new role must be different from the old one");
    }
}
