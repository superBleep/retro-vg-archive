package com.superbleep.rvga.exception;

import com.superbleep.rvga.exception.general.BadRequestException;

public class EmulatorEmptyBody extends BadRequestException {
    public EmulatorEmptyBody() {
        super("Request must modify at least one field");
    }
}
