package com.superbleep.rvga.exception;

import com.superbleep.rvga.exception.general.BadRequestException;

public class GameVersionEmptyBody extends BadRequestException {
    public GameVersionEmptyBody() {
        super("Request must modify at least one field");
    }
}
