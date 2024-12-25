package com.superbleep.rvga.exception;

import com.superbleep.rvga.exception.general.BadRequestException;

public class PlatformEmptyBody extends BadRequestException {
    public PlatformEmptyBody() {
        super("Request must modify at least one field");
    }
}
