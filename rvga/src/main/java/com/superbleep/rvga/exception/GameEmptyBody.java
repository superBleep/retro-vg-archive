package com.superbleep.rvga.exception;

import com.superbleep.rvga.exception.general.BadRequestException;

public class GameEmptyBody extends BadRequestException {
    public GameEmptyBody() {
        super("Request must modify at least one field");
    }
}
