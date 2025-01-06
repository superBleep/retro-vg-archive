package com.superbleep.rvga.exception;

import com.superbleep.rvga.exception.general.BadRequestException;

public class EmulatorNotFound extends BadRequestException {
    public EmulatorNotFound(long id) {
        super(STR."Emulator with id \{id} doesn't exist in the database");
    }
}
