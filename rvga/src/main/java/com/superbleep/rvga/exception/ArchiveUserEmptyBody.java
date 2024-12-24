package com.superbleep.rvga.exception;

import com.superbleep.rvga.exception.general.BadRequestException;

public class ArchiveUserEmptyBody extends BadRequestException {
    public ArchiveUserEmptyBody() {
        super("Request must modify at least one field");
    }
}
