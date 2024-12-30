package com.superbleep.rvga.exception;

import com.superbleep.rvga.exception.general.NotFoundException;

public class GameNotFound extends NotFoundException {
    public GameNotFound(long id) {
        super(STR."Game with id \{id} doesn't exist in the database");
    }
}
