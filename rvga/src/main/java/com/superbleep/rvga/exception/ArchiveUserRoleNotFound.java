package com.superbleep.rvga.exception;

import com.superbleep.rvga.exception.general.BadRequestException;

public class ArchiveUserRoleNotFound extends BadRequestException {
    public ArchiveUserRoleNotFound() {
        super("Request user role not found");
    }
}
