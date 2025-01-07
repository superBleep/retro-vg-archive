package com.superbleep.rvga.exception;

import com.superbleep.rvga.exception.general.NotFoundException;

public class EmulatorNotFound extends NotFoundException {
    public EmulatorNotFound(long id) {
        super(STR."Emulator with id \{id} doesn't exist in the database");
    }
}
