package com.superbleep.rvga.exception;

import com.superbleep.rvga.exception.general.ForbiddenException;

public class ArchiveUserRolesForbidden extends ForbiddenException {
    public ArchiveUserRolesForbidden() {
        super("Only admins can change the roles of other users");
    }
}
