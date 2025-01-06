package com.superbleep.rvga.exception;

import com.superbleep.rvga.exception.general.BadRequestException;

public class EmulatorEmptyPlatformList extends BadRequestException {
    public EmulatorEmptyPlatformList() {
        super("Platform list must not be empty");
    }
}
