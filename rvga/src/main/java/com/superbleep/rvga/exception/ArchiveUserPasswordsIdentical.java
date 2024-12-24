package com.superbleep.rvga.exception;

import com.superbleep.rvga.exception.general.BadRequestException;

public class ArchiveUserPasswordsIdentical extends BadRequestException {
    public ArchiveUserPasswordsIdentical() {
        super("The new password must be different from the old one");
    }
}
